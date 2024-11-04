package fr.floz.epicurean.data.mapper

import fr.floz.epicurean.data.services.local.CuisineEntity
import fr.floz.epicurean.domain.entities.Cuisine

object CuisineMapper : Mapper<CuisineEntity, Cuisine> {

    override fun mapFromEntity(type: CuisineEntity): Cuisine {
        return Cuisine(
            id = type.id,
            label = type.label
        )
    }

    override fun mapToEntity(type: Cuisine): CuisineEntity {
        return CuisineEntity(
            id = type.id,
            label = type.label
        )
    }
}