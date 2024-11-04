package fr.floz.epicurean.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.floz.epicurean.domain.repo.ElementsRepository
import fr.floz.epicurean.ui.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ElementsListViewModel @Inject constructor(
    private val repository: ElementsRepository
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.LAST_EDITED)
    private val _restaurants = _sortType
        .flatMapLatest { s ->
            when(s) {
                SortType.LAST_EDITED -> repository.getAllElementsOrderedByLastEdited()
                SortType.ALPHABETICALLY -> repository.getAllElementsOrderedByNameAsc()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ElementsListUiState())
    val state = combine(_state, _sortType, _restaurants) { state, sortType, restaurants ->
        state.copy(
            elements = restaurants,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ElementsListUiState())


    /**
     * Traitement des événements de l'ui
     */
    fun onEvent(event: ElementsListEvent) {
        when(event) {
            is ElementsListEvent.SortRestaurants -> {
                _sortType.value = event.sortType
            }
            ElementsListEvent.ShowMap -> {
                // TODO
            }
        }
    }

}