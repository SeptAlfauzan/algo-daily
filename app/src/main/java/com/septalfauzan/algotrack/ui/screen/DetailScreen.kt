package com.septalfauzan.algotrack.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.domain.model.ui.UiState
import com.septalfauzan.algotrack.helper.formatTimeStampDatasource
import com.septalfauzan.algotrack.helper.formatTimeStampDatasourceHourMinute
import com.septalfauzan.algotrack.ui.component.ErrorHandler
import com.septalfauzan.algotrack.ui.utils.bottomBorder
import com.septalfauzan.algotrack.ui.utils.shimmer
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    attendanceId: String,
    loadDetail: (String) -> Unit,
    reloadDetail: () -> Unit,
    detailStateUi: StateFlow<UiState<AttendanceEntity>>
) {
    val textStyle: TextStyle = MaterialTheme.typography.body1
    var latitude by rememberSaveable { mutableStateOf(0.0) }
    var longitude by rememberSaveable { mutableStateOf(0.0) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }

    var attendanceDate by rememberSaveable { mutableStateOf("") }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(latitude, longitude), 40f
        )
    }

    fun updateAttendanceDateTimeTitle(data: AttendanceEntity) {
        attendanceDate = data.timestamp.formatTimeStampDatasourceHourMinute()
    }

    fun updateLocation(lat: Double?, lon: Double?) {
        if (lat != null && lon != null) {
            latitude = lat
            longitude = lon
        }
    }

    fun updateDataIsLoaded(status: Boolean) {
        dataLoaded = status
    }

    LaunchedEffect(dataLoaded) {
        if (dataLoaded) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(LatLng(latitude, longitude), 15f, 0f, 0f)
                ),
                durationMs = 500
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = stringResource(id = R.string.attendance_detail, attendanceDate.formatTimeStampDatasource())) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
        },
    ) { _ ->
        detailStateUi.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    ShimmerLoading()
                    loadDetail(attendanceId)
                }
                is UiState.Success -> {
                    updateDataIsLoaded(true)
                    updateAttendanceDateTimeTitle(uiState.data)
                    updateLocation(lat = uiState.data.latitude, lon = uiState.data.longitude)
                    Column(
                        modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.detail), style = MaterialTheme.typography.h6)
                        DetailScreenItem(
                            label = stringResource(id = R.string.question1),
                            text = when (uiState.data.status) {
                                AttendanceStatus.PERMIT -> stringResource(id = R.string.permit)
                                AttendanceStatus.ON_DUTY -> stringResource(id = R.string.on_duty)
                                AttendanceStatus.OFF_DUTY -> stringResource(id = R.string.off_duty)
                                AttendanceStatus.NOT_FILLED -> stringResource(id = R.string.not_filled)
                            },
                            textStyle = textStyle
                        )
                        uiState.data.reason?.let {
                            DetailScreenItem(
                                label = stringResource(id = R.string.question2),
                                text = it,
                                textStyle = textStyle
                            )
                        }
                        DetailScreenItem(
                            label = stringResource(id = R.string.time_stamp),
                            text = uiState.data.timestamp.formatTimeStampDatasource(),
                            textStyle = textStyle,
                        )
                        Box(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                                .bottomBorder(
                                    width = 1.dp,
                                    borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                                )
                        )
                        Text(
                            text = stringResource(id = R.string.location),
                            style = textStyle,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .height(248.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
                        ) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                uiSettings = MapUiSettings(
                                    myLocationButtonEnabled = true,
                                    compassEnabled = true,
                                    zoomControlsEnabled = false,
                                    zoomGesturesEnabled = false,
                                    scrollGesturesEnabled = false,
                                    scrollGesturesEnabledDuringRotateOrZoom = false,
                                ),
                                properties = MapProperties(isMyLocationEnabled = false),
                            ) {
                                Marker(
                                    state = MarkerState(position = LatLng(latitude, longitude)),
                                    icon = BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_CYAN
                                    ),
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorHandler(reload = reloadDetail, errorMessage = uiState.errorMessage)
                }
            }
        }

    }
}

@Composable
private fun ShimmerLoading() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier
                .height(32.dp)
                .width(240.dp)
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            Modifier
                .height(32.dp)
                .width(120.dp)
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            Modifier
                .height(32.dp)
                .width(160.dp)
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            Modifier
                .height(32.dp)
                .width(280.dp)
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            Modifier
                .height(32.dp)
                .width(280.dp)
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(248.dp)
                .fillMaxWidth()
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun DetailScreenItem(
    label: String,
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label, style = textStyle.copy(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                fontWeight = FontWeight(300)
            )
        )
        Text(
            text = text, style = textStyle.copy(
                color = MaterialTheme.colors.primary,
            )
        )
    }
}
