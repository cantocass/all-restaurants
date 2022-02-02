//package com.cassidy.allrestaurants
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.ContextCompat
//import javax.inject.Inject
//
//class CheckUserLocationPermissionUseCase @Inject constructor() {
//
//    /**
//     * need: context, activity
//     * viewmodel methods
//     * ui-thread safe
//     *
//     * put an event on the ui stream?
//     * store a couple of callbacks
//     * consumed/initiated by viewmodel?
//     * expose own observable?
//     */
//    private fun checkHasLocationPermission() {
//        Log.d("locationDebug", "checkHasLocationPermission()")
//ActivityCompat.reque
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            browseViewModel.onUserUpdatesLocationPermission(true)
//        } else {
//            val requestPermissionLauncher =
//                registerForActivityResult(
//                    ActivityResultContracts.RequestPermission()
//                ) { isGranted: Boolean ->
//                    if (isGranted) {
//                        browseViewModel.onUserUpdatesLocationPermission(true)
//                    } else {
//                        browseViewModel.onUserUpdatesLocationPermission(false)
//                    }
//                }
//
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//}

//if (ActivityCompat.checkSelfPermission(
//this,
//Manifest.permission.ACCESS_FINE_LOCATION
//) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//this,
//Manifest.permission.ACCESS_COARSE_LOCATION
//) != PackageManager.PERMISSION_GRANTED
//) {
//    // TODO: Consider calling
//    //    ActivityCompat#requestPermissions
//    // here to request the missing permissions, and then overriding
//    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//    //                                          int[] grantResults)
//    // to handle the case where the user grants the permission. See the documentation
//    // for ActivityCompat#requestPermissions for more details.
//    return
//}