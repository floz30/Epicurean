package fr.floz.epicurian.ui.list

import fr.floz.epicurian.domain.entities.Element
import fr.floz.epicurian.ui.SortType

data class ElementsListUiState(
    val elements: List<Element> = emptyList(),
    val sortType: SortType = SortType.LAST_EDITED
)
