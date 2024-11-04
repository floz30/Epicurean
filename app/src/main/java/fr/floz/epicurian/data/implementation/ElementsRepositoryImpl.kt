package fr.floz.epicurian.data.implementation

import fr.floz.epicurian.data.mapper.CuisineMapper
import fr.floz.epicurian.data.mapper.ElementMapper
import fr.floz.epicurian.data.mapper.ElementWithCuisineMapper
import fr.floz.epicurian.data.services.local.ElementDao
import fr.floz.epicurian.domain.entities.Cuisine
import fr.floz.epicurian.domain.entities.Element
import fr.floz.epicurian.domain.repo.ElementsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ElementsRepositoryImpl @Inject constructor(
    private val elementDao: ElementDao
) : ElementsRepository {

    override suspend fun deleteElement(element: Element) {
        elementDao.deleteElement(
            ElementMapper.mapToEntity(element)
        )
    }

    override fun getAllElementsOrderedByLastEdited(): Flow<List<Element>> {
        return elementDao.getAllOrderedByLastEdited().map {
            it.map(ElementWithCuisineMapper::mapFromEntity)
        }
    }

    override fun getAllElementsOrderedByNameAsc(): Flow<List<Element>> {
        return elementDao.getAllOrderedByNameAsc().map {
            it.map(ElementWithCuisineMapper::mapFromEntity)
        }
    }

    override fun getAllElementsOrderedByNameDesc(): Flow<List<Element>> {
        return elementDao.getAllOrderedByNameDesc().map {
            it.map(ElementWithCuisineMapper::mapFromEntity)
        }
    }

    override fun getAllCuisinesOrderedByNameAsc(): Flow<List<Cuisine>> {
        return elementDao.getAllCuisinesOrderedByNameAsc().map {
            it.map(CuisineMapper::mapFromEntity)
        }
    }

    override fun getElementById(elementId: Int): Flow<Element> {
        return elementDao
            .getElementWithCuisine(elementId)
            .map(ElementWithCuisineMapper::mapFromEntity)
    }

    override suspend fun saveElement(element: Element) {
        elementDao.insertElementWithCuisines(
            element = ElementMapper.mapToEntity(element),
            cuisines = element.cuisines.map(CuisineMapper::mapToEntity)
        )
    }

    override suspend fun updateElement(element: Element) {
        elementDao.updateElement(
            ElementMapper.mapToEntity(
                element.copy(
                    lastEditedAt = System.currentTimeMillis()
                )
            )
        )
    }
}