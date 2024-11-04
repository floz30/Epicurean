package fr.floz.epicurian.domain.entities


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
}