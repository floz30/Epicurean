package fr.floz.epicurian.ui.restaurant

import fr.floz.epicurian.data.Restaurant
import fr.floz.epicurian.ui.home.SortType

data class RestaurantListState(
    val restaurants: List<Restaurant> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val selectedRestaurantId: Long? = null,
    val isAddingRestaurant: Boolean = false,
    val name: String = "",
    val location: String = "",
    val sortType: SortType = SortType.LAST_ADDED
)
