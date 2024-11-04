package fr.floz.epicurean.ui.list

import fr.floz.epicurean.ui.SortType

sealed interface ElementsListEvent {

    data class SortRestaurants(val sortType: SortType): ElementsListEvent

    data object ShowMap: ElementsListEvent

}