package fr.floz.epicurian.data

import kotlinx.coroutines.flow.Flow

/**
 * An interface to get all restaurants saved.
 */
interface RestaurantsRepository {
    fun getAllOrderedByLastCreated(): Flow<List<Restaurant>>

    fun getAllOrderedByName(): Flow<List<Restaurant>>

    suspend fun insertRestaurant(name: String, location: String)

    suspend fun updateRestaurant(restaurant: Restaurant)

    suspend fun deleteRestaurant(restaurant: Restaurant)

}