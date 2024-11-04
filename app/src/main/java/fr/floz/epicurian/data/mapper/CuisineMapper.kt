package fr.floz.epicurian.data.mapper

import fr.floz.epicurian.data.services.local.CuisineEntity
import fr.floz.epicurian.domain.entities.Cuisine

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