package fr.floz.epicurean.data

import androidx.room.TypeConverter
import de.westnordost.osm_opening_hours.model.OpeningHours
import de.westnordost.osm_opening_hours.parser.toOpeningHoursOrNull
import fr.floz.epicurean.domain.entities.ElementType

class Converters {

    @TypeConverter
    fun stringToOpeningHours(value: String?): OpeningHours? {
        return value?.toOpeningHoursOrNull()
    }

    @TypeConverter
    fun openingHoursToString(value: OpeningHours?) : String? {
        return value?.toString()
    }

    @TypeConverter
    fun elementTypeToString(value: ElementType?): String? {
        return value?.name
    }

    @TypeConverter
    fun stringToElementType(value: String): ElementType {
        return try {
            ElementType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            ElementType.UNKNOWN
        }

    }
}