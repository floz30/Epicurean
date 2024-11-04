package fr.floz.epicurian.domain.repo

import fr.floz.epicurian.domain.entities.Cuisine
import fr.floz.epicurian.domain.entities.Element
import kotlinx.coroutines.flow.Flow

/**
 * An interface to get all restaurants saved.
 */
interface ElementsRepository {

    /**
     * Gets all elements ordered by last edited time in descending order.
     */
    fun getAllElementsOrderedByLastEdited(): Flow<List<Element>>

    /**
     * Gets all elements ordered by name in ascending order.
     */
    fun getAllElementsOrderedByNameAsc(): Flow<List<Element>>

    /**
     * Gets all elements ordered by name in descending order.
     */
    fun getAllElementsOrderedByNameDesc(): Flow<List<Element>>


    fun getAllCuisinesOrderedByNameAsc(): Flow<List<Cuisine>>

    /**
     * Gets the element info details.
     *
     * @param elementId the id of the restaurant
     */
    fun getElementById(elementId: Int): Flow<Element>

    /**
     * Save a new element.
     */
    suspend fun saveElement(element: Element)

    /**
     * Update the specified element record.
     */
    suspend fun updateElement(element: Element)

    /**
     * Delete the specified element record.
     *
     * @param element the element record to delete
     */
    suspend fun deleteElement(element: Element)

}