package fr.floz.epicurian.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Delete
    suspend fun delete(restaurant: Restaurant)

    @Upsert
    suspend fun upsert(restaurant: Restaurant)

    @Query("SELECT * FROM Restaurant ORDER BY name ASC")
    fun getAllOrderedByName(): Flow<List<Restaurant>>

    @Query("SELECT * FROM Restaurant ORDER BY created_at DESC")
    fun getAllOrderedByLastCreated(): Flow<List<Restaurant>>

    @Query("SELECT * FROM Restaurant WHERE id = :restaurantId")
    fun get(restaurantId: Int): Flow<Restaurant?>
}