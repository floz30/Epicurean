package fr.floz.epicurian.data.services.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OverpassResponse(
    val version: Float = 0f,
    val generator: String = "",
    @SerialName("elements")
    val osmElements: List<OsmElement> = emptyList()
)

