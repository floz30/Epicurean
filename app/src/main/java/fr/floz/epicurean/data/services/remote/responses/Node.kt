package fr.floz.epicurean.data.services.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("node")
data class Node(
    override val id: Long,
    override val tags: Map<String, String> = emptyMap(),

    @SerialName("lat")
    val latitude: Double,
    @SerialName("lon")
    val longitude: Double,
) : OsmElement() {

    @SerialName("type")
    override val type
        get() = OsmElementType.NODE

    init {
        checkValidity(latitude, longitude)
    }

    companion object {
        fun checkValidity(latitude: Double, longitude: Double) {
            require(latitude >= -90.0 && latitude <= 90
                        && longitude >= -180 && longitude <= 180
            ) { "Latitude $latitude, longitude $longitude is not a valid position" }
        }
    }
}
