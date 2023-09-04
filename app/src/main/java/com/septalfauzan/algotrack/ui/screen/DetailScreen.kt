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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.helper.formatTimeStampDatasource
import com.septalfauzan.algotrack.helper.formatTimeStampDatasourceHourMinute
import com.septalfauzan.algotrack.ui.component.ErrorHandler
import com.septalfauzan.algotrack.ui.utils.bottomBorder
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
    val context = LocalContext.current
    val textStyle: TextStyle = MaterialTheme.typography.body1
    val mapPosition = LatLng( 37.3875,  -122.0575)
    var attendanceDate by rememberSaveable{ mutableStateOf("") }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(mapPosition, 40f) }
    fun updateAttendanceDateTimeTitle(data: AttendanceEntity){ attendanceDate = data.timestamp.formatTimeStampDatasourceHourMinute()  }
//    val mapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Absen $attendanceDate") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
    ) {  _ ->
        detailStateUi.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when(uiState){
                is UiState.Loading -> {
                    CircularProgressIndicator()
                    loadDetail(attendanceId)
                }
                is UiState.Success -> {
                    updateAttendanceDateTimeTitle(uiState.data)
                    Column(
                        modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Rincian", style = MaterialTheme.typography.h6)
                        DetailScreenItem(
                            label = "Apakah anda sedang bekerja?",
                            text = when(uiState.data.status){
                                AttendanceStatus.PERMIT -> "Izin"
                                AttendanceStatus.ON_DUTY -> "Masuk"
                                AttendanceStatus.OFF_DUTY -> "Cuti"
                                AttendanceStatus.NOT_FILLED -> "Belum Absen"
                            },
                            textStyle = textStyle
                        )
                        uiState.data.reason?.let{
                            DetailScreenItem(
                                label = "Alasan tidak bekerja",
                                text =  it,
                                textStyle = textStyle
                            )
                        }
                        DetailScreenItem(
                            label = "Timestamp",
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
                            text = "Lokasi",
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
                            ){
                                Marker(
                                    state = MarkerState(position = mapPosition),
                                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                                )
//                    Circle(center = mapPosition, radius = pulse.toDouble(), fillColor = MaterialTheme.colors.secondary.copy(alpha = 0.3f), strokeColor = MaterialTheme.colors.primary, strokeWidth = 0f)
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

//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//private fun Preview() {
//    AlgoTrackTheme() {
//        Surface {
//            DetailScreen(
//                navController = rememberNavController(),
//                attendanceId = "id",
//                loadDetail = { id -> historyAttendanceViewModel.getDetail(id) },
//                reloadDetail = { historyAttendanceViewModel.reloadDetail() },
//                detailStateUi = historyAttendanceViewModel.detail
//            )
//        }
//    }
//}
