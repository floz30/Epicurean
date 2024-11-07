package fr.floz.epicurean.domain.entities


data class Cuisine(
    val id: Long = 0,
    val label: String
) {

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            label,
            label.replace('_', ' ')
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    override fun toString(): String {
        return label
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cuisine) return false
        return this.label == other.label
    }

    override fun hashCode(): Int {
        return label.hashCode()
    }


}