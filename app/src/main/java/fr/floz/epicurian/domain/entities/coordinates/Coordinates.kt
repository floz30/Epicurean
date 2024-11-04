package fr.floz.epicurian.domain.entities.coordinates


interface Coordinates {
    companion object {
        val defaultGps = Gps(0.0, 0.0)
        val defaultMercator = Mercator(0.0, 0.0)
    }
}