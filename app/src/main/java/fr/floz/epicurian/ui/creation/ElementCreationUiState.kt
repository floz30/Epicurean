package fr.floz.epicurian.ui.creation

import fr.floz.epicurian.domain.entities.Cuisine
import fr.floz.epicurian.domain.entities.Element
import fr.floz.epicurian.domain.entities.ElementType
import fr.floz.epicurian.domain.entities.coordinates.Coordinates
import fr.floz.epicurian.domain.entities.coordinates.Gps

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

    val userLocation: Gps = Gps(0.0, 0.0),
    val isUserLocationShow: Boolean = false

)