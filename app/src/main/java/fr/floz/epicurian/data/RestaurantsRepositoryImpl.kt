package fr.floz.epicurian.data

import fr.floz.epicurian.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Static data of [Restaurant].
 */
class RestaurantsRepositoryImpl : RestaurantsRepository {

    override fun getAllRestaurants(): Flow<List<Restaurant>> = flow {
        emit(listOf(
            Restaurant(
                1L,
                "Nom restaurant 1",
                "Thai",
                "Gagny",
                R.drawable.ic_launcher_background),
            Restaurant(
                2L,
                "Nom restaurant 2",
                "Française",
                "Chelles",
                R.drawable.ic_launcher_background),
            Restaurant(
                3L,
                "Nom restaurant 3",
                "Italienne",
                "Noisy le Grand",
                R.drawable.ic_launcher_background)
        ))
    }
}