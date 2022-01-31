package com.cassidy.allrestaurants

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearbyPlacesRepository @Inject constructor(private val placesApi: GooglePlacesApi) {
    suspend fun getNearbyRestaurants(location: LatLngLiteral?): List<Place> {
        if (location == null) {
            return listOf()
        }

        val response = placesApi.getNearbyPlace("${location.lat},${location.lng}")
        return response.results
    }
}