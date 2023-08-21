package com.septalfauzan.algotrack.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.septalfauzan.algotrack.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var map: GoogleMap? by remember { mutableStateOf(null) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission granted, update user's location
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        map?.addMarker(
                            MarkerOptions()
                                .position(userLatLng)
                                .title("Your Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        )
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                    }
                }
            } catch (securityException: SecurityException) {
                // Handle the exception if permission is revoked
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
                }
            )
        },
        content = {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    MapView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { mapView ->
                    val mapFragment = (context as AppCompatActivity).supportFragmentManager.findFragmentByTag("mapFragment") as? SupportMapFragment
                    if (mapFragment == null) {
                        val newMapFragment = SupportMapFragment.newInstance()
                        val transaction = context.supportFragmentManager.beginTransaction()
                        transaction.add(mapView.id, newMapFragment, "mapFragment").commit()
                        newMapFragment.getMapAsync { googleMap ->
                            map = googleMap
                            googleMap.uiSettings.isZoomControlsEnabled = true

                            try {
                                val styleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
                                googleMap.setMapStyle(styleOptions)
                            } catch (e: Resources.NotFoundException) {
                                // Handle the exception if style file is not found
                            }

                            if (PermissionUtils.isLocationPermissionGranted(context)) {
                                try {
                                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                        location?.let {
                                            val userLatLng = LatLng(it.latitude, it.longitude)
                                            googleMap.addMarker(
                                                MarkerOptions()
                                                    .position(userLatLng)
                                                    .title("Your Location")
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                            )
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                                        }
                                    }
                                } catch (securityException: SecurityException) {
                                    // Handle the exception if permission is revoked
                                }
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    } else {
                        mapFragment.getMapAsync { googleMap ->
                            map = googleMap
                            googleMap.uiSettings.isZoomControlsEnabled = true

                            try {
                                val styleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
                                googleMap.setMapStyle(styleOptions)
                            } catch (e: Resources.NotFoundException) {
                                // Handle the exception if style file is not found
                            }
                        }
                    }
                }
            )
        }
    )
}
object PermissionUtils {
    fun isLocationPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}