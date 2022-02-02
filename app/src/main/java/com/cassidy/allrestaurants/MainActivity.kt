package com.cassidy.allrestaurants

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cassidy.allrestaurants.common.OnLocationReadyCallback
import com.cassidy.allrestaurants.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint

//Not done
//The user may select a search result to display basic information about the restaurant
//test out bad permission flow
//on item selected -> DetailFragment
//UIEvents on a LiveData/flow
//performance improvements & improving data flow
//savedInstanceState for mapview/location
//so much UI polish
//use ui spec
//flag restaurant as favorite
//persist user favorites locally
//not production ready
//any sort of testing

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnLocationReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val browseViewModel: BrowseNearbyRestaurantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        checkHasLocationPermission()
        browseViewModel.onRequestUserLocation(this::onLocationReady)
        browseViewModel.fetchData()

        handleIntent(intent)
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            isSubmitButtonEnabled = true
        }

        return true
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.d("searchDebug", "Text handled: $query")
            browseViewModel.onNewQuerySubmitted(query)
        }
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