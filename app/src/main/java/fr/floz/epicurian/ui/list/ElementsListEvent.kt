package fr.floz.epicurian.ui.list

import fr.floz.epicurian.ui.SortType

sealed interface ElementsListEvent {

    data class SortRestaurants(val sortType: SortType): ElementsListEvent

    data object ShowMap: ElementsListEvent

}