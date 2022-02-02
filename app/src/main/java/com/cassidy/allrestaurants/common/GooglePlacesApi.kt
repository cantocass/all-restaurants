package com.cassidy.allrestaurants.common

import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("nearbysearch/json?radius=5000&type=restaurant")
    suspend fun getNearbyPlace(@Query("location") latlong : String) : GooglePlacesNearbyResponse


//    .url("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=mongolian&inputtype=textquery&locationbias=circle%3A2000%4047.6918452%2C-122.2226413&fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry&key=YOUR_API_KEY")
//    .url("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry&key=YOUR_API_KEY")

}