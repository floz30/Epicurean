package fr.floz.epicurean.data.services.remote

import fr.floz.epicurean.data.services.remote.responses.OverpassResponse

interface OverpassApi {

    suspend fun getElementsAroundLocation(latitude: Double, longitude: Double): OverpassResponse

}