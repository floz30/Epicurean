package fr.floz.epicurean.data.services.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("way")
data class Way(
    override val id: Long,
    override val tags: Map<String, String> = emptyMap(),

    @SerialName("nodes")
    val nodeIds: List<Long>,
    @SerialName("bounds")
    val bounds: Bounds
) : OsmElement() {

    @SerialName("type")
    override val type
        get() = OsmElementType.WAY

}
