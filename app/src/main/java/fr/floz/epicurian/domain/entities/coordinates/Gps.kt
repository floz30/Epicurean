package fr.floz.epicurian.domain.entities.coordinates

import androidx.room.ColumnInfo


data class Gps(
    @ColumnInfo("latitude")
    val latitude: Double,
    @ColumnInfo("longitude")
    val longitude: Double
) : Coordinates