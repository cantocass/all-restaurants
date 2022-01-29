package com.cassidy.allrestaurants

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.cassidy.allrestaurants.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

//TODO
//- Fragments or Jetpack Compose for UI, comprising at least two distinct screens (e.g. list/detail)
//- Reactive programming (either RxJava or Flow)
//- Modern android development practices (Jetpack, Clean Architecture)
//- Local database and/or Network call to retrieve data that is populated to some portion of the UI
//- Optional Elements: Dependency Injection, Unit Tests

//Done
//The app will use the Google Places API for its data source
//Upon launch, the app will execute a search that displays NEARBY restaurants

//Priorities
//Retrofit/OKHttp, Dagger/Hilt
//The app will prompt the user for permission to access their current location


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

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val sydney = LatLng(-34.0, 151.0)


    @Inject
    lateinit var googlePlacesApi: GooglePlacesApi

    @Inject
    lateinit var locationServicesStaticWrapper: GoogleLocationServicesStaticWrapper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        lifecycleScope.launch {
//            Log.d("request", "Starting Request")
//            val response = googlePlacesApi.getNearbyPlace("${sydney.latitude},${sydney.longitude}")
//            Log.d("request", "result = ${response.results.size}")
//        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}