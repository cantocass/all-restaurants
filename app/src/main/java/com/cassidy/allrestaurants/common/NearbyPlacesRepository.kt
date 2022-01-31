package com.cassidy.allrestaurants.common

import android.util.Log
import com.cassidy.allrestaurants.LatLngLiteral
import com.cassidy.allrestaurants.Place
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearbyPlacesRepository @Inject constructor(private val placesApi: GooglePlacesApi) {
    suspend fun getNearbyRestaurants(location: LatLngLiteral?): List<Place> {
        Log.d("locationDebug", "getNearbyRestaurants()")

        if (location == null) {
            return listOf()
        }

        val response = placesApi.getNearbyPlace("${location.lat},${location.lng}")
        Log.d("locationDebug", "getNearbyRestaurants = ${location.lat}, ${location.lng}, results[${response.results.size}]")

        return response.results
    }
}