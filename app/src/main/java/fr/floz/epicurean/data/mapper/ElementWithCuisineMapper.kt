package fr.floz.epicurean.data.mapper

import fr.floz.epicurean.data.services.local.ElementWithCuisines
import fr.floz.epicurean.domain.entities.Element

object ElementWithCuisineMapper : Mapper<ElementWithCuisines, Element> {

    override fun mapFromEntity(type: ElementWithCuisines): Element {
        val element = ElementMapper.mapFromEntity(type.element)
        return element.copy(
            cuisines = type.cuisines.map(CuisineMapper::mapFromEntity)
        )
    }

    override fun mapToEntity(type: Element): ElementWithCuisines {
        val element = ElementMapper.mapToEntity(type)
        return ElementWithCuisines(
            element = element,
            cuisines = type.cuisines.map(CuisineMapper::mapToEntity)
        )
    }
}