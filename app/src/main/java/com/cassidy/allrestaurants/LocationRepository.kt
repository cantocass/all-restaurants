package com.cassidy.allrestaurants

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(//private val staticWrapper: GoogleLocationServicesStaticWrapper,
                                             private val placesApi: GooglePlacesApi) {


}