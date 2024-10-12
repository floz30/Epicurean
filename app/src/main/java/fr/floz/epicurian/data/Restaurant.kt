package fr.floz.epicurian.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An object which represents a restaurant.
 */
@Entity
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    //val type: String,
    val location: String,
//    @DrawableRes val image: Int,
//    val image: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    //val tags: Set<String> = emptySet()
)
