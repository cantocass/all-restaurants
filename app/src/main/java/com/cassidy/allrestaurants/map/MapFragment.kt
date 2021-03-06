package com.cassidy.allrestaurants.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cassidy.allrestaurants.BrowseNearbyRestaurantsViewModel
import com.cassidy.allrestaurants.R
import com.cassidy.allrestaurants.common.Place
import com.cassidy.allrestaurants.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val browseViewModel by activityViewModels<BrowseNearbyRestaurantsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync {
            Log.d("mapDebug", "onMapReady()")
            browseViewModel.onGoogleMapReady(it)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()


        browseViewModel.observableState.observe(this) {
            Log.d("mapDebug", "observedScreenState = ${it.location?.lat}, ${it.location?.lng}, results[${it.restaurantsList.size}]")
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
        }
    }

    override fun onResume() {
        super.onResume()

        binding.listButton.setOnClickListener { browseViewModel.onListButtonClick(this::navigateToListFragment) }
    }

    override fun onStop() {
        browseViewModel.observableState.removeObservers(this)

        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun navigateToListFragment() {
        findNavController().navigate(R.id.action_mapFragment_to_listFragment)
    }
}