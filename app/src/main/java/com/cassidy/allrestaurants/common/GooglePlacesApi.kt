package com.cassidy.allrestaurants.common

import com.cassidy.allrestaurants.GooglePlacesNearbyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("nearbysearch/json?radius=5000&type=restaurant")
    suspend fun getNearbyPlace(@Query("location") latlong : String) : GooglePlacesNearbyResponse

}