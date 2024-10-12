package fr.floz.epicurian.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.floz.epicurian.data.RestaurantsRepository
import fr.floz.epicurian.ui.RestaurantEvent
import fr.floz.epicurian.ui.restaurant.RestaurantListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RestaurantsRepository
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.LAST_ADDED)
    private val _restaurantId = MutableStateFlow(0)
    private val _restaurants = _sortType
        .flatMapLatest { _sortType ->
            when(_sortType) {
                SortType.LAST_ADDED -> repository.getAllOrderedByLastCreated()
                SortType.ALPHABETICALLY -> repository.getAllOrderedByName()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(RestaurantListState())
    val state = combine(_state, _sortType, _restaurants) { state, sortType, restaurants ->
        state.copy(
            restaurants = restaurants,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), RestaurantListState())


    /**
     * Traitement des événements de l'ui
     */
    fun onEvent(event: RestaurantEvent) {
        when(event) {
            RestaurantEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingRestaurant = false
                ) }
            }
            RestaurantEvent.SaveRestaurant -> {
                // Récupération des valeurs
                val name = state.value.name
                val location = state.value.location
                if (name.isBlank() || location.isBlank()) {
                    return // rien à faire si pas de valeur
                }

                // Ajout en bdd
                viewModelScope.launch {
                    repository.insertRestaurant(name, location)
                }

                // Maj de l'ui
                _state.update { it.copy(
                    isAddingRestaurant = false,
                    name = "",
                    location = ""
                ) }
            }
            is RestaurantEvent.SetLocation -> {
                _state.update { it.copy(
                    location = event.location
                ) }
            }
            is RestaurantEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            RestaurantEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingRestaurant = true
                ) }
            }
            is RestaurantEvent.SortRestaurants -> {
                _sortType.value = event.sortType
            }
        }
    }

}