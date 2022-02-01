package com.cassidy.allrestaurants.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cassidy.allrestaurants.BrowseNearbyRestaurantsViewModel
import com.cassidy.allrestaurants.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    @Inject
    lateinit var restaurantListAdapter: RestaurantListAdapter
    private val browseViewModel by activityViewModels<BrowseNearbyRestaurantsViewModel>()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        checkHasLocationPermission()
//        viewModelBrowse.onRequestUserLocation(this::onLocationReady)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater)

           with(binding.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = restaurantListAdapter
            }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        browseViewModel.fetchData()

        browseViewModel.observableState.observe(this) {
            Log.d("locationDebug", "observedScreenState = ${it.location?.lat}, ${it.location?.lng}, results[${it.restaurantsList.size}]")


            restaurantListAdapter.submitList(it.restaurantsList)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}