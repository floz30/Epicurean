package fr.floz.epicurian.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Static data of [Restaurant].
 */
@Singleton
class RestaurantsRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao
) : RestaurantsRepository {

    override suspend fun deleteRestaurant(restaurant: Restaurant) {
        restaurantDao.delete(restaurant)
    }

    override fun getAllOrderedByLastCreated(): Flow<List<Restaurant>> {
        return restaurantDao.getAllOrderedByLastCreated()
    }

    override fun getAllOrderedByName(): Flow<List<Restaurant>> {
        return restaurantDao.getAllOrderedByName()
    }

    override suspend fun insertRestaurant(name: String, location: String) {
        restaurantDao.upsert(
            Restaurant(
                name = name,
                location = location,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateRestaurant(restaurant: Restaurant) {
        restaurantDao.upsert(restaurant)
    }
}