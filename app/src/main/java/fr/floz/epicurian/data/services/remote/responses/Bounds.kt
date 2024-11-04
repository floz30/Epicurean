package fr.floz.epicurian.data.services.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bounds")
data class Bounds(
    @SerialName("minlat")
    val minLatitude: Double,
    @SerialName("minlon")
    val minLongitude: Double,
    @SerialName("maxlat")
    val maxLatitude: Double,
    @SerialName("maxlon")
    val maxLongitude: Double,
) {

    val center: Pair<Double, Double>
        get() = Pair((maxLatitude + minLatitude) / 2, (minLongitude + maxLongitude) / 2)

}