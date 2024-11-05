package fr.floz.epicurean.ui.creation

import android.location.Location
import fr.floz.epicurean.domain.entities.Cuisine
import fr.floz.epicurean.domain.entities.Element
import fr.floz.epicurean.domain.entities.ElementType
import fr.floz.epicurean.domain.entities.coordinates.Coordinates
import fr.floz.epicurean.domain.entities.coordinates.Gps

data class ElementCreationUiState(
    // form
    val name: String = "",
    val type: ElementType = ElementType.UNKNOWN,
    val location: Gps = Coordinates.defaultGps,
    val cuisineTextFieldContent: String = "",
    val cuisine: List<Cuisine> = emptyList(),
    val openingHours: String = "",
    val phone: String = "",
    val website: String = "",
    val isWheelchairAccessible: Boolean = false,
    val streetName: String = "",
    val houseNumber: String = "",
    val city: String = "",
    val postCode: String = "",

    // form error
    val isNameError: Boolean = false,
    val isElementTypeError: Boolean = false,

    // osm dialog
    val osmElements: List<Element> = emptyList(),
    val selectedElement: Element? = null,
    val showOsmDialog: Boolean = false,

    // search cuisine dialog
    val showSearchDialog: Boolean = false,

    val userLocation: Location? = null

)