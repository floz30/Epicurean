package fr.floz.epicurean.domain.entities

import de.westnordost.osm_opening_hours.model.OpeningHours
import fr.floz.epicurean.domain.entities.coordinates.Gps


data class Element(
    val id: Long = 0,
    val name: String,
    val type: ElementType,
    val location: Gps,
    val openingHours: OpeningHours?,
    val phone: String?,
    val website: String?,
    val isWheelchairAccessible: Boolean,
    val address: Address,
    val lastEditedAt: Long,
    val cuisines: List<Cuisine>
)