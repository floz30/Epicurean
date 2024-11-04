package fr.floz.epicurean.data.services.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {

    @Delete
    suspend fun deleteElement(restaurant: ElementEntity)

    @Update
    suspend fun updateElement(restaurant: ElementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: ElementEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuisines(cuisines: List<CuisineEntity>) : List<Long>

    @Insert
    suspend fun insertElementCuisineCrossRef(crossRef: ElementCuisineCrossRef)

    @Transaction
    suspend fun insertElementWithCuisines(element: ElementEntity, cuisines: List<CuisineEntity>) {
        val elementId = insertElement(element)
        val cuisinesId = insertCuisines(cuisines)
        cuisinesId.forEach { cuisineId ->
            insertElementCuisineCrossRef(
                ElementCuisineCrossRef(
                    elementId,
                    cuisineId
                )
            )
        }
    }

    @Query("SELECT * FROM cuisines ORDER BY label ASC")
    fun getAllCuisinesOrderedByNameAsc(): Flow<List<CuisineEntity>>

    @Transaction
    @Query("SELECT * FROM elements ORDER BY name ASC")
    fun getAllOrderedByNameAsc(): Flow<List<ElementWithCuisines>>

    @Transaction
    @Query("SELECT * FROM elements ORDER BY name DESC")
    fun getAllOrderedByNameDesc(): Flow<List<ElementWithCuisines>>

    @Transaction
    @Query("SELECT * FROM elements ORDER BY last_edited_at DESC")
    fun getAllOrderedByLastEdited(): Flow<List<ElementWithCuisines>>

    @Transaction
    @Query("SELECT * FROM elements WHERE id = :elementId")
    fun getElementWithCuisine(elementId: Int): Flow<ElementWithCuisines>
}