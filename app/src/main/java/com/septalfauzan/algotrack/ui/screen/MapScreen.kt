package com.septalfauzan.algotrack.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.domain.model.UserLocation
import com.septalfauzan.algotrack.ui.component.RoundedButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val locationPermissions28Above = listOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
)

val locationPermissions = listOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
)

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf(UserLocation(0.0, 0.0)) }

    val userLatLng = LatLng(userLocation.latitude, userLocation.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLatLng, 40f)
    }

    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 5f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val permissionsState =
        rememberMultiplePermissionsState(permissions = if (Build.VERSION.SDK_INT > 28) locationPermissions28Above else locationPermissions)

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

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        when(permissionsState.allPermissionsGranted){
            true -> {
                try {
                    coroutineScope.launch(Dispatchers.IO) {
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                } catch (securityException: SecurityException) {
                    // Handle exception if location updates cannot be requested
                    securityException.printStackTrace()
                }
            }
            else -> permissionsState.launchMultiplePermissionRequest()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            } catch (securityException: SecurityException) {
                // Handle exception if location updates cannot be removed
                securityException.printStackTrace()
            }
        }
    }
//

    LaunchedEffect(userLocation.latitude, userLocation.longitude) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition(LatLng(userLocation.latitude, userLocation.longitude), 40f, 0f, 0f)
            ),
            durationMs = 400
        )
    }
    val mapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        content = {
            PermissionsRequired(
                multiplePermissionsState = permissionsState,
                permissionsNotGrantedContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Text(text = stringResource(id = R.string.need_request_permission), style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                        ))
                        RoundedButton(text = stringResource(id = R.string.request_permission), onClick = { permissionsState.launchMultiplePermissionRequest() })
                    }
                },
                permissionsNotAvailableContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(id = R.string.request_permission_not_granted))
                    }
                },
            ){
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true,
                        compassEnabled = true,
                    ),
                    properties = MapProperties(isMyLocationEnabled = true, mapStyleOptions = mapStyle),
                ){
                    Marker(
                        state = MarkerState(position = userLatLng),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                    )
                    Circle(center = userLatLng, radius = pulse.toDouble(), fillColor = MaterialTheme.colors.secondary.copy(alpha = 0.3f), strokeColor = MaterialTheme.colors.primary, strokeWidth = 0f)
                }
            }
        }
    )
}