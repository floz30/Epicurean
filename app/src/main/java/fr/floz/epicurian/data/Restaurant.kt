package fr.floz.epicurian.data

import androidx.annotation.DrawableRes

/**
 * An object which represents a restaurant.
 */
data class Restaurant(
    val id: Long,
    val name: String,
    val type: String,
    val location: String,
    @DrawableRes val image: Int
)
