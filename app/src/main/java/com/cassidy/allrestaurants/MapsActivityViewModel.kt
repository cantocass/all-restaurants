package com.cassidy.allrestaurants

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsActivityViewModel @Inject constructor(private val nearbyPlacesRepository: NearbyPlacesRepository,
                                                private val googleLocationWrapper: GoogleLocationServicesStaticWrapper,
                                                private val stateFactory: ScreenStateFactory): ViewModel() {

    private var liveState : MutableLiveData<ScreenState> = MutableLiveData<ScreenState>(stateFactory.createInitialState())
    val observableState : LiveData<ScreenState> = Transformations.distinctUntilChanged(liveState)

    fun fetchData() {
        viewModelScope.launch {

            if (liveState.value!!.hasLocationPermission && liveState.value!!.location != null) {
                val places = nearbyPlacesRepository.getNearbyRestaurants(liveState.value!!.location)
                liveState.value = stateFactory.updateStateWithPlaceList(liveState.value!!, places)
            } else {
                //todo who knows
            }


        }
    }

    fun onUserUpdatesLocationPermission(hasLocationPermission: Boolean) {
        liveState.value = stateFactory.updateStateWithLocationPermission(liveState.value!!, hasLocationPermission)
    }

    fun onRequestUserLocation(callback: (LocationResult) -> Unit) {
        googleLocationWrapper.initiateUserLocationRequest(callback)
    }

    fun onLocationReady(result: LocationResult) {
        liveState.value = stateFactory.updateStateWithLocationData(liveState.value!!,
            LatLngLiteral(result.lastLocation.latitude, result.lastLocation.longitude))
    }

    fun onGoogleMapReady(googleMap: GoogleMap?) {
        liveState.value = stateFactory.updateStateWithGoogleMap(liveState.value!!, googleMap)
    }
}

data class ScreenState constructor(val restaurantsList: List<Place>,
                                   val hasLocationPermission: Boolean,
                                   val location: LatLngLiteral?,
                                   val googleMap: GoogleMap?)

class ScreenStateFactory @Inject constructor() {
    fun createInitialState() = ScreenState(listOf(), false, null, null)
    fun updateStateWithPlaceList(currentState: ScreenState, updatedRestaurantsList: List<Place>) = currentState.copy(restaurantsList = updatedRestaurantsList)
    fun updateStateWithLocationPermission(currentState: ScreenState, updatedPermission: Boolean) = currentState.copy(hasLocationPermission = updatedPermission)
    fun updateStateWithLocationData(currentState: ScreenState, updatedLocation: LatLngLiteral) = currentState.copy(location = updatedLocation)
    fun updateStateWithGoogleMap(currentState: ScreenState, updatedGoogleMap: GoogleMap?) = currentState.copy(googleMap = updatedGoogleMap)
}