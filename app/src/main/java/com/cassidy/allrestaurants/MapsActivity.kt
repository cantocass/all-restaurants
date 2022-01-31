package com.cassidy.allrestaurants

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.cassidy.allrestaurants.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import kotlinx.coroutines.ExperimentalCoroutinesApi


//TODO
//- Fragments or Jetpack Compose for UI, comprising at least two distinct screens (e.g. list/detail)
//- Reactive programming (either RxJava or Flow)
//- Modern android development practices (Jetpack, Clean Architecture)
//- Local database and/or Network call to retrieve data that is populated to some portion of the UI
//- Optional Elements: Dependency Injection, Unit Tests

//Done
//The app will use the Google Places API for its data source
//Upon launch, the app will execute a search that displays NEARBY restaurants
//Retrofit/OKHttp, Dagger/Hilt
//The app will prompt the user for permission to access their current location

//Priorities
//test out bad permission flow


//Required Features
//A search feature will be included that allows the user to search for restaurants
//The user may choose to display the search results as a list, or as pins on a map
//The user may select a search result to display basic information about the restaurant
//Project Requirements
//You may use the API key “AIzaSyDue_S6t9ybh_NqaeOJDkr1KC9a2ycUYuE”.
//This is not an unlimited key and may only be used for this assignment.
//The developer may use whatever language they choose
//The developer may make use of third party libraries if they choose, however they should NOT use Google's Places API client
//The code should be written with a production level of quality, as if it were going to be distributed as-is
//Bonus Points
//Allow the user to flag restaurants as a favorite, and indicate its favorite status in the current and future search results
//Implement UI based on design requirements, UI Specifications, Assets
//Your completed code should be shared with AllTrails via GitHub.

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnLocationReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var viewModel: MapsActivityViewModel

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel = ViewModelProvider(this)[MapsActivityViewModel::class.java]
        checkHasLocationPermission()
        viewModel.onRequestUserLocation(this::onLocationReady)




        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchData()

        viewModel.observableState.observe(this) {
//            val location = LatLng(it.location?.lat ?: 0.0, it.location?.lng ?: 0.0)
//            if (it.googleMap != null && it.location != null) {
//            this.googleMap = it.googleMap
//
//                    googleMap.addMarker(
//                MarkerOptions().position(location)
//                    .title("Marker at ${location.latitude} and ${location.longitude}")
//            )
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
//            }

            //todo create binding model, bind, update ui
        }
    }

    override fun onStop() {
        viewModel.observableState.removeObservers(this)

        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.onGoogleMapReady(googleMap)
    }

    private fun checkHasLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.onUserUpdatesLocationPermission(true)
        } else {
            val requestPermissionLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        viewModel.onUserUpdatesLocationPermission(true)
                    } else {
                        viewModel.onUserUpdatesLocationPermission(false)
                    }
                }

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onLocationReady(result: LocationResult) {
        viewModel.onLocationReady(result)
    }
}