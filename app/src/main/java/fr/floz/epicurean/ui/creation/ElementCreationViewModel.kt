package fr.floz.epicurean.ui.creation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.westnordost.osm_opening_hours.parser.toOpeningHoursOrNull
import fr.floz.epicurean.data.mapper.CoordinatesMapper
import fr.floz.epicurean.data.services.LocationServices
import fr.floz.epicurean.data.services.remote.OverpassApi
import fr.floz.epicurean.data.services.remote.responses.Node
import fr.floz.epicurean.data.services.remote.responses.OverpassResponse
import fr.floz.epicurean.data.services.remote.responses.Way
import fr.floz.epicurean.domain.entities.Address
import fr.floz.epicurean.domain.entities.Cuisine
import fr.floz.epicurean.domain.entities.Element
import fr.floz.epicurean.domain.entities.ElementType
import fr.floz.epicurean.domain.entities.coordinates.Coordinates
import fr.floz.epicurean.domain.entities.coordinates.Gps
import fr.floz.epicurean.domain.entities.coordinates.Mercator
import fr.floz.epicurean.domain.entities.toElementType
import fr.floz.epicurean.domain.repo.ElementsRepository
import fr.floz.epicurean.utils.doublePulseEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.api.onLongPress
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.layout.Forced
import ovh.plrapps.mapcompose.ui.state.MapState
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.math.pow


@HiltViewModel
class ElementCreationViewModel @Inject constructor(
    private val repository: ElementsRepository,
    private val overpassApi: OverpassApi,
    private val locationServices: LocationServices
) : ViewModel() {

    private val _osmElements = MutableStateFlow(emptyList<Element>())
    private val _state = MutableStateFlow(ElementCreationUiState())
    val state = combine(_state, _osmElements) { state, osmElements ->
        state.copy(
            osmElements = osmElements
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ElementCreationUiState())

    var isLocationSelected by mutableStateOf(false)
        private set

    fun setLocationSelectedOrSkipped(value: Boolean) {
        isLocationSelected = value
    }

    // Search bar
    private val _searchCuisineText = MutableStateFlow("")
    val searchCuisineText = _searchCuisineText.asStateFlow()

    private val _cuisines = repository.getAllCuisinesOrderedByNameAsc().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val cuisines = searchCuisineText
        .combine(_cuisines) { text, cuisines ->
            if (text.isBlank()) {
                cuisines
            } else {
                cuisines.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _cuisines.value)

    private val tileStreamProvider = TileStreamProvider { row, col, zoom ->
        try {
            val url = URL("https://tile.openstreetmap.org/$zoom/$col/$row.png")
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Chrome/120.0.0.0 Safari/537.36")
            connection.doInput = true
            connection.connect()
            BufferedInputStream(connection.inputStream)
        } catch (e: Exception) {
            e.printStackTrace()

            null
        }
    }

    private val maxLevel = 16
    private val minLevel = 12
    private val mapSize = 256 * 2.0.pow(maxLevel).toInt()
    private val defaultScroll = CoordinatesMapper.mapFromEntity(Gps(48.86, 2.35)) // PARIS

    val mapState = MapState(
        levelCount = maxLevel + 1,
        fullWidth = mapSize,
        fullHeight = mapSize,
        workerCount = 16
    ) {
        // Initial values
        minimumScaleMode(Forced((1 / 2.0.pow(maxLevel - minLevel)).toFloat()))
        scroll(defaultScroll.x, defaultScroll.y)
    }.apply {
        addLayer(tileStreamProvider)
        scale = 0.5F
        onLongPress {x, y ->
            onLongTap(x, y)
        }
    }

    /**
     * If userLocation is not null, add a marker to user's position and zoom on it.
     */
    private fun showAndZoomOnUserLocation() {
        if (_state.value.userLocation == null) {
            // TODO avertir l'utilisateur
        } else {
            val location = CoordinatesMapper.mapFromEntity(Gps(_state.value.userLocation!!.latitude, _state.value.userLocation!!.longitude))

            createUserMarker(location)

            viewModelScope.launch {
                mapState.centerOnMarker("userLocation", destScale = 0.7f)
            }
        }
    }

    val showToast = mutableStateOf(false)


    /**
     * Displays a blue circle over the user's location.
     */
    private fun createUserMarker(location: Mercator) {
        mapState.removeMarker("userLocation")
        mapState.addMarker("userLocation", location.x, location.y, clickable =false) {
            Box(Modifier
                .doublePulseEffect(targetScale = 5.5f, duration = 2500)
                .requiredSize(10.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E90FF), CircleShape)
                .border(1.dp, Color(0xFFFFFFFF), CircleShape)
            )
        }
    }

    // permission
    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeAt(0)
    }
    fun onPermissionResult(permission: String, isGranted: Boolean, onGrantedPermission: () -> Unit) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        } else if (isGranted) {
            onGrantedPermission()
        }
    }


    private fun onLongTap(x: Double, y: Double) {
        val gpsCoordinates = CoordinatesMapper.mapToEntity(Mercator(x, y))
        viewModelScope.launch {
            val response = overpassApi.getElementsAroundLocation(gpsCoordinates.latitude, gpsCoordinates.longitude)
            if (response.osmElements.isNotEmpty()) {
                _osmElements.value = mapOverpassResponseToRestaurants(response)
                _state.update { it.copy(
                    showOsmDialog = true
                ) }
            }
        }
    }

    private fun mapOverpassResponseToRestaurants(response: OverpassResponse): List<Element> {
        return response.osmElements.map { it ->
            val location: Gps = when (it) {
                is Node -> Gps(it.latitude, it.longitude)
                is Way -> Gps(it.bounds.center.first, it.bounds.center.second)
            }
            val id = it.id
            val name = it.tags["name"]!!
            val cuisine = it.tags["cuisine"]
            val elementType = it.tags["amenity"].toElementType()
            val openingHours = it.tags["opening_hours"]
            val phone = it.tags["phone"]
            val website = it.tags["website"]
            val isWheelchairAccessible = it.tags["wheelchair"].toBoolean()

            val houseNumber = it.tags["addr:housenumber"]?.toInt()
            val streetName = it.tags["addr:street"]
            val postcode = it.tags["addr:postcode"]?.toInt()
            val city = it.tags["addr:city"]

            val address = Address(houseNumber, streetName, postcode, city)

            Element(
                id = id,
                name = name,
                type = elementType,
                location = location,
                cuisines = cuisine?.split(";")?.map { label -> Cuisine(label = label) } ?: emptyList(),
                phone = phone,
                website = website,
                isWheelchairAccessible = isWheelchairAccessible,
                address = address,
                openingHours = openingHours?.toOpeningHoursOrNull(),
                lastEditedAt = System.currentTimeMillis()
            )
        }
    }

    /**
     * UI event handling.
     */
    fun onEvent(event: ElementCreationEvent) {
        when (event) {
            ElementCreationEvent.SaveElement -> {
                val name = state.value.name
                val type = state.value.type
                val location = state.value.location
                val cuisine = state.value.cuisine
                val openingHours = state.value.openingHours
                val phone = state.value.phone
                val website = state.value.website
                val isWheelchairAccessible = state.value.isWheelchairAccessible
                val address = Address(
                    houseNumber = state.value.houseNumber.toInt(),
                    streetName = state.value.streetName,
                    postCode = state.value.postCode.toInt(),
                    city = state.value.city
                )

                if (name.isBlank()) {
                    return // check required values
                }

                viewModelScope.launch {
                    repository.saveElement(
                        Element(
                            name = name,
                            type = type,
                            location = location,
                            lastEditedAt = System.currentTimeMillis(),
                            cuisines = cuisine,
                            openingHours = openingHours.toOpeningHoursOrNull(),
                            phone = phone,
                            website = website,
                            isWheelchairAccessible = isWheelchairAccessible,
                            address = address
                        )
                    )
                }

                clearFormFields()
            }
            is ElementCreationEvent.SetStreetName -> {
                _state.update { it.copy(
                    streetName = event.streetName
                ) }
            }
            is ElementCreationEvent.SetHouseNumber -> {
                _state.update { it.copy(
                    houseNumber = event.houseNumber
                ) }
            }
            is ElementCreationEvent.SetCity -> {
                _state.update { it.copy(
                    city = event.city
                ) }
            }
            is ElementCreationEvent.SetPostCode -> {
                _state.update { it.copy(
                    postCode = event.postCode
                ) }
            }
            is ElementCreationEvent.AddCuisine -> {
                _state.update { it.copy(
                    cuisine = it.cuisine + event.cuisine
                ) }
            }
            is ElementCreationEvent.RemoveCuisine -> {
                _state.update { it.copy(
                    cuisine = it.cuisine - event.cuisine
                ) }
            }
            is ElementCreationEvent.SetCuisineTextFieldContent -> {
                _state.update { it.copy(
                    cuisineTextFieldContent = event.cuisine
                ) }
            }
            is ElementCreationEvent.SetElementType -> {
                _state.update { it.copy(
                    type = event.elementType
                ) }
            }
            is ElementCreationEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is ElementCreationEvent.SetOpeningHours -> {
                _state.update { it.copy(
                    openingHours = event.openingHours
                ) }
            }
            is ElementCreationEvent.SetPhone -> {
                _state.update { it.copy(
                    phone = event.phone
                ) }
            }
            is ElementCreationEvent.SetWebsite -> {
                _state.update { it.copy(
                    website = event.website
                ) }
            }
            is ElementCreationEvent.SetLocation -> {
                _state.update { it.copy(
                    location = event.location
                ) }
            }
            is ElementCreationEvent.SetWheelchairAccessibility -> {
                _state.update { it.copy(
                    isWheelchairAccessible = event.isWheelchairAccessible
                ) }
            }
            ElementCreationEvent.ShowOsmDialog -> {
                clearFormFields()
                _state.update { it.copy(
                    selectedElement = null,
                    showOsmDialog = !it.showOsmDialog
                ) }
            }
            is ElementCreationEvent.SelectOsmElement -> {
                _state.update { it.copy(
                    selectedElement = event.element
                ) }
            }
            ElementCreationEvent.ShowCuisineDialog -> {
                _state.update { it.copy(
                    showSearchDialog = !it.showSearchDialog
                ) }
            }
        }
    }

    fun tempFun() {
        _state.update { it.copy(
            showSearchDialog = !it.showSearchDialog
        ) }
    }

    fun tempFun2(value: String) {
        _searchCuisineText.value = value
    }

    /**
     * Import data from OSM element extracted into the form.
     */
    fun importData() {
        val element = _state.value.selectedElement
        element?.let {
            _state.update { it.copy(
                name = element.name,
                type = element.type,
                location = element.location,
                cuisine = element.cuisines,
                phone = element.phone ?: "",
                website = element.website ?: "",
                isWheelchairAccessible = element.isWheelchairAccessible,
                houseNumber = element.address.houseNumber?.toString() ?: "",
                streetName = element.address.streetName ?: "",
                postCode = element.address.postCode?.toString() ?: "",
                city = element.address.city ?: "",
                openingHours = element.openingHours?.toString() ?: ""
            ) }
        }
    }


    /**
     * Reset fields at their default values.
     */
    private fun clearFormFields() {
        _state.update { it.copy(
            name = "",
            type = ElementType.UNKNOWN,
            location = Coordinates.defaultGps,
            cuisine = emptyList(),
            phone = "",
            website = "",
            isWheelchairAccessible = false,
            openingHours = "",
            houseNumber = "",
            streetName = "",
            postCode = "",
            city = ""
        ) }
    }

    /**
     * If permission is granted, get last known location depending, show it on the map and zoom on it.
     */
    @SuppressLint("MissingPermission")
    fun resolveLocation() {
        viewModelScope.launch {
            if (locationServices.isLocationPermissionAlreadyGranted()) {
                _state.update { it.copy(
                    userLocation = locationServices.resolveLocation()
                ) }
            }
        }.invokeOnCompletion { showAndZoomOnUserLocation() }

    }
}

