package com.cassidy.allrestaurants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cassidy.allrestaurants.databinding.ActivityMainBinding
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

//Priorities
//The user may choose to display the search results as a list, or as pins on a map
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
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}