package fr.floz.epicurean.data.services.remote.responses

import kotlinx.serialization.Serializable

@Serializable
sealed class OsmElement {
    abstract val id: Long
    abstract val tags: Map<String, String>
    abstract val type: OsmElementType
}

enum class OsmElementType {
    NODE,
    WAY
}