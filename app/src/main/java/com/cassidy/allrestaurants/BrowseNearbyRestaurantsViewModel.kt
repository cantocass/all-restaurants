package com.cassidy.allrestaurants

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassidy.allrestaurants.common.GoogleLocationServicesStaticWrapper
import com.cassidy.allrestaurants.common.LatLngLiteral
import com.cassidy.allrestaurants.common.NearbyPlacesRepository
import com.cassidy.allrestaurants.common.Place
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseNearbyRestaurantsViewModel @Inject constructor(private val nearbyPlacesRepository: NearbyPlacesRepository,
                                                           private val googleLocationWrapper: GoogleLocationServicesStaticWrapper,
                                                           private val stateFactory: ScreenStateFactory): ViewModel() {

    private var liveState : MutableLiveData<ScreenState> = MutableLiveData<ScreenState>(stateFactory.createInitialState())
    val observableState : LiveData<ScreenState> = Transformations.distinctUntilChanged(liveState)



    fun fetchData() {
        Log.d("locationDebug", "fetchData()")
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
        Log.d("locationDebug", "onUserUpdatesLocationPermission()")
        liveState.value = stateFactory.updateStateWithLocationPermission(liveState.value!!, hasLocationPermission)
    }

    fun onRequestUserLocation(callback: (LocationResult) -> Unit) {
        Log.d("locationDebug", "onRequestUserLocation()")
        googleLocationWrapper.initiateUserLocationRequest(callback)
    }

    fun onLocationReady(result: LocationResult) {
        Log.d("locationDebug", "onLocationReady()")
        val snapshotState = liveState.value!!
        val newLocationState = stateFactory.updateStateWithLocationData(liveState.value!!,
            LatLngLiteral(result.lastLocation.latitude, result.lastLocation.longitude)
        )
        liveState.value = stateFactory.updateStateWithLocationData(liveState.value!!,
            LatLngLiteral(result.lastLocation.latitude, result.lastLocation.longitude)
        )
        if (snapshotState != newLocationState) {
            fetchData()
        }
    }

    fun onGoogleMapReady(googleMap: GoogleMap?) {
        Log.d("locationDebug", "onGoogleMapReady()")
        liveState.value = stateFactory.updateStateWithGoogleMap(liveState.value!!, googleMap)
    }

    fun onListButtonClick(navigationFunction: () -> Unit) {
        navigationFunction.invoke()
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
    fun updateStateWithLocationData(currentState: ScreenState, updatedLocation: LatLngLiteral): ScreenState {
        //lose some accuracy to reduce model updates
        val simpleLatitude = updatedLocation.lat.times(1000).toInt().toDouble().div(1000)
        val simpleLongitude = updatedLocation.lng.times(1000).toInt().toDouble().div(1000)
        val simpleLocation = LatLngLiteral(simpleLatitude, simpleLongitude)
        return currentState.copy(location = simpleLocation)
    }
    fun updateStateWithGoogleMap(currentState: ScreenState, updatedGoogleMap: GoogleMap?) = currentState.copy(googleMap = updatedGoogleMap)
}