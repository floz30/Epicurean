package fr.floz.epicurean.data.services.remote

import android.util.Log
import fr.floz.epicurean.data.services.remote.responses.OverpassResponse
import fr.floz.epicurean.utils.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import javax.inject.Inject


class OverpassApiImpl @Inject constructor(
    private val client: HttpClient
) : OverpassApi {

    /**
     * Recherche tous les éléments possédant un tag amenity correspondant à l'une des valeurs suivantes :
     * <ol>
     *     <li>restaurant</li>
     *     <li>cafe</li>
     *     <li>fast_food</li>
     *     <li>food_court</li>
     *     <li>ice_cream</li>
     *     <li>pub</li>
     *     <li>bar</li>
     * </ol>
     */
    override suspend fun getElementsAroundLocation(
        latitude: Double,
        longitude: Double
    ): OverpassResponse {
        val url = URLBuilder().apply {
            takeFrom(AppConstants.OVERPASS_API_URL)
        }
        val body = "data=[out:json][timeout:25];nwr(around:30.0,$latitude,$longitude)[amenity~\"^(restaurant|cafe|fast_food|food_court|ice_cream|pub|bar)\$\"];out geom;"
        Log.d("OsmApiImpl.getRestaurantsAroundLocation", "Requête OverpassAPI avec latitude=$latitude et longitude=$longitude")
        return client.post(url.build()) {
            setBody(body)
        }.body()
    }

}
