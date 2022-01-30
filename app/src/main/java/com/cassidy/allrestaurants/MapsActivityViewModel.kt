package com.cassidy.allrestaurants

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsActivityViewModel @Inject constructor(private val locationRepository: LocationRepository,
                                        private val googlePlacesApi: GooglePlacesApi,
                                        private val stateFactory: ScreenStateFactory): ViewModel() {

    private val liveState : MutableLiveData<ScreenState> = MutableLiveData<ScreenState>(stateFactory.createInitialState())
    val observableState : LiveData<ScreenState> = liveState

    fun fetchData(location: LatLng) {
        viewModelScope.launch {
            Log.d("request", "Starting Request")
            val response = googlePlacesApi.getNearbyPlace("${location.latitude},${location.longitude}")
            Log.d("request", "result = ${response.results.size}")

            stateFactory.updateStateWithPlaceList(liveState.value!!, response.results)
        }
    }

    fun onUserUpdatesLocationPermission(hasLocationPermission: Boolean) {
        stateFactory.updateStateWithLocationPermission(liveState.value!!, hasLocationPermission)
    }
}

data class ScreenState constructor(val restaurantsList: List<Place>,
                                   val hasLocationPermission: Boolean)

class ScreenStateFactory @Inject constructor() {
    fun createInitialState() = ScreenState(listOf(), false)
    fun updateStateWithPlaceList(currentState: ScreenState, updatedRestaurantsList: List<Place>) = currentState.copy(restaurantsList = updatedRestaurantsList)
    fun updateStateWithLocationPermission(currentState: ScreenState, updatedPermission: Boolean) = currentState.copy(hasLocationPermission = updatedPermission)

}