package com.septalfauzan.algotrack.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.*
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.helper.formatTimeStampDatasource
import com.septalfauzan.algotrack.helper.formatToLocaleGMT
import com.septalfauzan.algotrack.presentation.AttendanceViewModel
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.ButtonType
import com.septalfauzan.algotrack.ui.component.EdtTextAttandence
import com.septalfauzan.algotrack.ui.component.RoundedButton

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AttendanceScreen(
    id: String,
    createdAt: String,
    navController: NavController,
    viewModel: AttendanceViewModel
) {
    var currentQuestion by remember { mutableStateOf(1) }
    var selectedAnswer by remember { mutableStateOf("") }
    var reasonNotWork by remember { mutableStateOf("") }
    var latitude: Double by remember { mutableStateOf(0.0) }
    var longitude: Double by remember { mutableStateOf(0.0) }
    var isLoadLocationFinished: Boolean by rememberSaveable { mutableStateOf(false) }
    var errorMessage: String? by remember { mutableStateOf(null) }

    val offsetQuestion1 by animateDpAsState(targetValue = if (currentQuestion == 1) 0.dp else (-1000).dp)
    val offsetQuestion2 by animateDpAsState(targetValue = if (currentQuestion == 2) 0.dp else (1000).dp)
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val permissionsState =
        rememberMultiplePermissionsState(permissions = if (Build.VERSION.SDK_INT > 28) locationPermissions28Above else locationPermissions)

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val latestLocation = locationResult.lastLocation
            latestLocation?.let {
                Log.d("TAG", "onLocationResult: $it")
                latitude = it.latitude
                longitude = it.longitude
            }
            isLoadLocationFinished = true
        }
    }

    LaunchedEffect(Unit) {
        runLocationRequest(permissionsState, fusedLocationClient, locationCallback)
        viewModel.eventFlow.collect { event ->
            when (event) {
                is MyEvent.MessageEvent -> errorMessage = event.message
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onDisposeLocationRequest(fusedLocationClient, locationCallback)
        }
    }


    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Attendance ${createdAt.formatToLocaleGMT().formatTimeStampDatasource()}",
                            style = MaterialTheme.typography.h6,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.surface,
                )
            }
        ) {
            if (!isLoadLocationFinished) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Harap tunggu sebentar",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    CircularProgressIndicator()
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .offset(x = offsetQuestion1)
                    ) {
                        Text(
                            text = "Attendance",
                            style = MaterialTheme.typography.h6,
                        )
                        Text(
                            text = "Question $currentQuestion/2",
                            style = MaterialTheme.typography.subtitle1,
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight()
                    ) {
                        if (currentQuestion == 1) {
                            Column(modifier = Modifier.offset(x = offsetQuestion1)) {
                                Text(
                                    text = "Apakah anda sedang bekerja?",
                                    style = MaterialTheme.typography.h5,
                                )
                                Text(
                                    text = "pilih sesuai kondisi anda sekarang",
                                    style = MaterialTheme.typography.h6,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedAnswer == "Yes",
                                        onClick = { selectedAnswer = "Yes" },
                                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Yes",
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedAnswer == "No",
                                        onClick = { selectedAnswer = "No" },
                                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "No",
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (selectedAnswer == "Yes") {
                                    RoundedButton(
                                        onClick = {
                                            viewModel.createAttendance(
                                                id = id,
                                                createdAt = createdAt,
                                                selectedAnswer = selectedAnswer,
                                                reasonNotWork = reasonNotWork,
                                                latitude = latitude,
                                                longitude = longitude,
                                                navController = navController,
                                            )
                                        },
                                        text = "kirim",
                                        buttonType = ButtonType.PRIMARY,
                                        onloading = viewModel.attendanceFormUiState.collectAsState().value.onLoading,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .align(Alignment.End),
                                        enabled = selectedAnswer.isNotBlank(),
                                    )
                                } else {
                                    RoundedButton(
                                        onClick = { currentQuestion = 2 },
                                        text = "selanjutnya",
                                        buttonType = ButtonType.PRIMARY,
                                        modifier = Modifier.align(Alignment.End),
                                        enabled = selectedAnswer.isNotBlank(),
                                    )
                                }
                            }
                        } else if (currentQuestion == 2) {
                            Column(modifier = Modifier.offset(x = offsetQuestion2)) {
                                Text(
                                    text = "Alasan anda tidak bekerja?",
                                    style = MaterialTheme.typography.h5,
                                )
                                EdtTextAttandence(
                                    label = "Ketik Disini",
                                    onChange = { reasonNotWork = it },
                                    value = reasonNotWork
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    RoundedButton(
                                        onClick = { currentQuestion = 1 },
                                        text = "sebelumnya",
                                        modifier = Modifier.width(150.dp)
                                    )
                                    RoundedButton(
                                        onClick = {
                                            viewModel.createAttendance(
                                                id = id,
                                                createdAt = createdAt,
                                                selectedAnswer = selectedAnswer,
                                                reasonNotWork = reasonNotWork,
                                                latitude = latitude,
                                                longitude = longitude,
                                                navController = navController,
                                            )
                                        },
                                        onloading = viewModel.attendanceFormUiState.collectAsState().value.onLoading,
                                        text = "kirim",
                                        buttonType = ButtonType.PRIMARY,
                                        modifier = Modifier.width(150.dp),
                                        enabled = reasonNotWork.isNotBlank(),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            errorMessage?.let { msg ->
                BottomSheetErrorHandler(message = msg, action = {
                    errorMessage = null
                }, dismissLabel = "tutup")
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
private fun runLocationRequest(
    permissionsState: MultiplePermissionsState,
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    val locationRequest = LocationRequest.create().apply {
        interval = 10000 // Update interval in milliseconds (e.g., every 10 seconds)
        fastestInterval = 5000 // Fastest interval in milliseconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Location accuracy priority
    }
    when (permissionsState.allPermissionsGranted) {
        true -> {
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (securityException: SecurityException) {
                // Handle exception if location updates cannot be requested
                securityException.printStackTrace()
            }
        }
        else -> permissionsState.launchMultiplePermissionRequest()
    }
}

private fun onDisposeLocationRequest(
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    try {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    } catch (securityException: SecurityException) {
        // Handle exception if location updates cannot be removed
        securityException.printStackTrace()
    }
}
