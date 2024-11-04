package fr.floz.epicurean.ui.list

import fr.floz.epicurean.domain.entities.Element
import fr.floz.epicurean.ui.SortType

data class ElementsListUiState(
    val elements: List<Element> = emptyList(),
    val sortType: SortType = SortType.LAST_EDITED
)
