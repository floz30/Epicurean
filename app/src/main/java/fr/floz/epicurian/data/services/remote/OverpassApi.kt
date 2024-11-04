package fr.floz.epicurian.data.services.remote

import fr.floz.epicurian.data.services.remote.responses.OverpassResponse

interface OverpassApi {

    suspend fun getElementsAroundLocation(latitude: Double, longitude: Double): OverpassResponse

}