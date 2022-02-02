package com.cassidy.allrestaurants.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cassidy.allrestaurants.BrowseNearbyRestaurantsViewModel
import com.cassidy.allrestaurants.R
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
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater)

           with(binding.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = restaurantListAdapter
            }

        binding.mapButton.setOnClickListener { browseViewModel.onMapButtonClick(this::navigateToMapFragment) }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        browseViewModel.observableState.observe(this) {
            Log.d("locationDebug", "observedScreenState = ${it.location?.lat}, ${it.location?.lng}, results[${it.restaurantsList.size}]")

            restaurantListAdapter.submitList(it.restaurantsList)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun navigateToMapFragment() {
        findNavController().navigate(R.id.action_listFragment_to_mapFragment)
    }
}