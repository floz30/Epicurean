package fr.floz.epicurian.data.mapper

import fr.floz.epicurian.data.services.local.ElementEntity
import fr.floz.epicurian.domain.entities.Element

object ElementMapper : Mapper<ElementEntity, Element> {

    override fun mapFromEntity(type: ElementEntity): Element {
        return Element(
            id = type.id,
            name = type.name,
            type = type.type,
            location = type.location,
            lastEditedAt = type.lastEditedAt,
            cuisines = emptyList(),
            openingHours = type.openingHours,
            phone = type.phoneNumber,
            website = type.website,
            isWheelchairAccessible = type.isWheelchairAccessible,
            address = type.address
        )
    }

    override fun mapToEntity(type: Element): ElementEntity {
        return ElementEntity(
            id = type.id,
            name = type.name,
            type = type.type,
            location = type.location,
            lastEditedAt = type.lastEditedAt,
            openingHours = type.openingHours,
            phoneNumber = type.phone,
            website = type.website,
            isWheelchairAccessible = type.isWheelchairAccessible,
            address = type.address
        )
    }
}