package fr.floz.epicurian.data.services.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.westnordost.osm_opening_hours.model.OpeningHours
import fr.floz.epicurian.domain.entities.Address
import fr.floz.epicurian.domain.entities.ElementType
import fr.floz.epicurian.domain.entities.coordinates.Gps

@Entity(tableName = "elements")
data class ElementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: ElementType,

    @Embedded(prefix = "location_")
    val location: Gps,

    @ColumnInfo("opening_hours")
    val openingHours: OpeningHours?,

    @ColumnInfo(name = "phone")
    val phoneNumber: String?,

    @ColumnInfo(name = "website")
    val website: String?,

    @ColumnInfo(name = "wheelchair")
    val isWheelchairAccessible: Boolean,

    @Embedded(prefix = "address_")
    val address: Address,

    @ColumnInfo(name = "last_edited_at")
    val lastEditedAt: Long,

)
