package fr.floz.epicurean.data.mapper

import fr.floz.epicurean.domain.entities.coordinates.Gps
import fr.floz.epicurean.domain.entities.coordinates.Mercator
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.sin


object CoordinatesMapper : Mapper<Gps, Mercator> {
    private const val EARTH_RADIUS = 6378137.0
    private const val X0 = -2.0037508342789248E7
    private const val TEMP = 2 * Math.PI / 360

    /**
     * Example: (48.86, 2.35) -> (0.5065277777777777, 0.34401404813997927)
     */
    override fun mapFromEntity(type: Gps): Mercator {
        require(abs(type.latitude) <= 90)
        require(abs(type.longitude) <= 180)

        val (xProjected, yProjected) = doProjection(type.latitude, type.longitude)
        val x = normalize(xProjected, X0, -X0)
        val y = normalize(yProjected, -X0, X0)
        return Mercator(x, y)
    }

    /**
     * Example: (0.5065277777777777, 0.34401404813997927) -> (48.85999999999999, 2.349999999999985)
     */
    override fun mapToEntity(type: Mercator): Gps {
        val xDenormalized = denormalize(type.x, X0, -X0)
        val yDenormalized = denormalize(type.y, -X0, X0)
        val (latitude, longitude) = undoProjection(xDenormalized, yDenormalized)
        return Gps(latitude, longitude)
    }

    /**
     * Conversion from WGS84 coordinates to EPSG:1024
     * Example: (48.86, 2.35) -> (261600.8033641929, 6251139.623506175)
     */
    private fun doProjection(latitude: Double, longitude: Double): Pair<Double, Double> {
        val num = longitude * TEMP
        val xValue = EARTH_RADIUS * num
        val a = latitude * TEMP
        val yValue = 3189068.5 * ln((1.0 + sin(a)) / (1.0 - sin(a)))

        return Pair(xValue, yValue)
    }

    /**
     * Conversion from EPSG:1024 coordinates to WGS84
     * Example: (261600.8033641929, 6251139.623506175) -> (48.85999999999999, 2.35)
     */
    private fun undoProjection(x: Double, y: Double): Gps {
        val a = x / EARTH_RADIUS
        val b = a * 57.29577951308232
        val c = floor((b + 180) / 360.0)
        val longitude = b - (c * 360)

        val d = 1.5707963267948966 - (2.0 * atan(exp(-y / EARTH_RADIUS)))
        val latitude = d * 57.29577951308232

        return Gps(latitude, longitude)
    }

    private fun normalize(t: Double, min: Double, max: Double): Double {
        return (t - min) / (max - min)
    }

    private fun denormalize(t: Double, min: Double, max: Double): Double {
        return t * (max - min) + min
    }

}






