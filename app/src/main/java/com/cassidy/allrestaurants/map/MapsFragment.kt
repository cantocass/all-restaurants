package com.cassidy.allrestaurants.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cassidy.allrestaurants.BrowseNearbyRestaurantsViewModel
import com.cassidy.allrestaurants.common.OnLocationReadyCallback
import com.cassidy.allrestaurants.common.Place
import com.cassidy.allrestaurants.R
import com.cassidy.allrestaurants.databinding.FragmentMapsBinding
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnLocationReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelBrowse: BrowseNearbyRestaurantsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("locationDebug", "onCreate()")
        viewModelBrowse = ViewModelProvider(this)[BrowseNearbyRestaurantsViewModel::class.java]
        checkHasLocationPermission()
        viewModelBrowse.onRequestUserLocation(this::onLocationReady)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync{
               Log.d("locationDebug", "onMapReady()")

            viewModelBrowse.onGoogleMapReady(it)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()

        Log.d("locationDebug", "onStart()")

        viewModelBrowse.fetchData()

        viewModelBrowse.observableState.observe(this) {
            Log.d("locationDebug", "observedScreenState = ${it.location?.lat}, ${it.location?.lng}, results[${it.restaurantsList.size}]")

            val userLocation = LatLng(it.location?.lat ?: 0.0, it.location?.lng ?: 0.0)
            if (it.googleMap != null && it.location != null) {
                it.googleMap.isMyLocationEnabled = true

                it.restaurantsList.forEach { place: Place ->
                    it.googleMap.addMarker(
                        MarkerOptions()
                            .title(place.name)
                            .position(
                                LatLng(
                                    place.geometry?.location?.lat ?: 0.0,
                                    place.geometry?.location?.lng ?: 0.0
                                )
                            )
                            .snippet("${place.rating} stars by ${place.userRatingsTotal} ratings \n" +
                                    "Price level: ${place.priceLevel} - ${place.businessStatus}")
                    )

                }
                it.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 11F))
            }
//            //todo create binding model, bind, update ui
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        binding.listButton.setOnClickListener { viewModelBrowse.onListButtonClick(this::navigateToListFragment) }
    }

    override fun onStop() {
        viewModelBrowse.observableState.removeObservers(this)

        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        Log.d("locationDebug", "onMapReady()")
//
//        viewModelBrowse.onGoogleMapReady(googleMap)
//    }

    private fun checkHasLocationPermission() {
        Log.d("locationDebug", "checkHasLocationPermission()")

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModelBrowse.onUserUpdatesLocationPermission(true)
        } else {
            val requestPermissionLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        viewModelBrowse.onUserUpdatesLocationPermission(true)
                    } else {
                        viewModelBrowse.onUserUpdatesLocationPermission(false)
                    }
                }

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onLocationReady(result: LocationResult) {
        Log.d("locationDebug", "onLocationReady()")

        viewModelBrowse.onLocationReady(result)
    }

    private fun navigateToListFragment() {
        findNavController().navigate(R.id.action_mapFragment_to_listFragment)
    }
}