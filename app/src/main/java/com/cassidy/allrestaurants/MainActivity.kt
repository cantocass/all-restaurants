package com.cassidy.allrestaurants

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cassidy.allrestaurants.common.OnLocationReadyCallback
import com.cassidy.allrestaurants.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint

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
//The search results are displayed as pins on a map
//The user may choose to display the search results as a list, or as pins on a map

//Priorities
//A search feature will be included that allows the user to search for restaurants
//The user may select a search result to display basic information about the restaurant

//test out bad permission flow

//Required Features
//Project Requirements
//This is not an unlimited key and may only be used for this assignment.
//The developer may use whatever language they choose
//The developer may make use of third party libraries if they choose, however they should NOT use Google's Places API client
//The code should be written with a production level of quality, as if it were going to be distributed as-is
//Bonus Points
//Allow the user to flag restaurants as a favorite, and indicate its favorite status in the current and future search results
//Implement UI based on design requirements, UI Specifications, Assets
//Your completed code should be shared with AllTrails via GitHub.

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnLocationReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val browseViewModel: BrowseNearbyRestaurantsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        checkHasLocationPermission()
        browseViewModel.onRequestUserLocation(this::onLocationReady)
        browseViewModel.fetchData()

//        if (Intent.ACTION_SEARCH == intent.action) {
//            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
////                doMySearch(query)
//            }
//        }

    }




    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    private fun checkHasLocationPermission() {
        Log.d("locationDebug", "checkHasLocationPermission()")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            browseViewModel.onUserUpdatesLocationPermission(true)
        } else {
            val requestPermissionLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        browseViewModel.onUserUpdatesLocationPermission(true)
                    } else {
                        browseViewModel.onUserUpdatesLocationPermission(false)
                    }
                }

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onLocationReady(result: LocationResult) {
        Log.d("locationDebug", "onLocationReady()")

        browseViewModel.onLocationReady(result)
    }
}