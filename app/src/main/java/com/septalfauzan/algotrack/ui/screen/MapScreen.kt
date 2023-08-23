package com.septalfauzan.algotrack.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.septalfauzan.algotrack.data.model.UserLocation

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf(UserLocation(0.0, 0.0)) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        userLocation = UserLocation(it.latitude, it.longitude)
                    }
                }
            } catch (securityException: SecurityException) {
                Log.d("Error in Permission Lanuncher", "$userLocation")
            }
        }
    }

    val locationRequest = LocationRequest.create().apply {
        interval = 10000 // Update interval in milliseconds (e.g., every 10 seconds)
        fastestInterval = 5000 // Fastest interval in milliseconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Location accuracy priority
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val latestLocation = locationResult.lastLocation
            latestLocation?.let {
                userLocation = UserLocation(it.latitude, it.longitude)
            }
        }
    }

    DisposableEffect(Unit) {
        if (PermissionUtils.isLocationPermissionGranted(context)) {
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            } catch (securityException: SecurityException) {
                // Handle exception if location updates cannot be requested
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        onDispose {
            try {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            } catch (securityException: SecurityException) {
                // Handle exception if location updates cannot be removed
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Map Screen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        content = {
            val userLatLng = LatLng(userLocation.latitude, userLocation.longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLatLng, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                content = {
                    if (PermissionUtils.isLocationPermissionGranted(LocalContext.current)) {
                        Marker(
                            state = MarkerState(position = userLatLng),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                        )
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    compassEnabled = true,
                ),
                properties = MapProperties(isMyLocationEnabled = true),
            )
        }
    )
}

object PermissionUtils {
    fun isLocationPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}