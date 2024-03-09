package fr.floz.epicurian.data

import kotlinx.coroutines.flow.Flow

/**
 * An interface to get all restaurants saved.
 */
interface RestaurantsRepository {
    fun getAllRestaurants(): Flow<List<Restaurant>>
}