package fr.floz.epicurian.domain.entities

import androidx.room.ColumnInfo

data class Address(
    @ColumnInfo("number")
    val houseNumber: Int? = null,
    @ColumnInfo("street")
    val streetName: String? = null,
    @ColumnInfo("post_code")
    val postCode: Int? = null,
    val city: String? = null
)