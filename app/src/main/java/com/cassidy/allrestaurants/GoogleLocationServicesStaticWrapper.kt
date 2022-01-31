package com.cassidy.allrestaurants

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLocationServicesStaticWrapper @Inject constructor(private val fusedLocationClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    fun initiateUserLocationRequest(callback: (LocationResult) -> Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                callback.invoke(result)
            }
        }

        val locationRequest = LocationRequest.create()
        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.fastestInterval = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    companion object {
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    }
}

interface OnLocationReadyCallback {
    fun onLocationReady(result: LocationResult)
}