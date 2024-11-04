package fr.floz.epicurian.ui.creation

import fr.floz.epicurian.domain.entities.Cuisine
import fr.floz.epicurian.domain.entities.Element
import fr.floz.epicurian.domain.entities.ElementType
import fr.floz.epicurian.domain.entities.coordinates.Gps

sealed interface ElementCreationEvent {
    // Set values of form
    data class SetName(val name: String): ElementCreationEvent
    data class SetElementType(val elementType: ElementType): ElementCreationEvent
    data class SetLocation(val location: Gps): ElementCreationEvent
    data class AddCuisine(val cuisine: Cuisine): ElementCreationEvent
    data class RemoveCuisine(val cuisine: Cuisine): ElementCreationEvent
    data class SetCuisineTextFieldContent(val cuisine: String): ElementCreationEvent
    data class SetOpeningHours(val openingHours: String): ElementCreationEvent
    data class SetPhone(val phone: String): ElementCreationEvent
    data class SetWebsite(val website: String): ElementCreationEvent
    data class SetWheelchairAccessibility(val isWheelchairAccessible: Boolean): ElementCreationEvent
    data class SetStreetName(val streetName: String): ElementCreationEvent
    data class SetHouseNumber(val houseNumber: String): ElementCreationEvent
    data class SetCity(val city: String): ElementCreationEvent
    data class SetPostCode(val postCode: String): ElementCreationEvent

    // Button action
    data object SaveElement: ElementCreationEvent
    data object ShowOsmDialog: ElementCreationEvent
    data class SelectOsmElement(val element: Element): ElementCreationEvent

    data object ShowCuisineDialog: ElementCreationEvent
}