package fr.floz.epicurian.ui

import fr.floz.epicurian.ui.home.SortType

sealed interface RestaurantEvent {
    data object SaveRestaurant: RestaurantEvent
    data class SetName(val name: String): RestaurantEvent
    data class SetLocation(val location: String): RestaurantEvent

    data object ShowDialog: RestaurantEvent
    data object HideDialog: RestaurantEvent

    data class SortRestaurants(val sortType: SortType): RestaurantEvent
}