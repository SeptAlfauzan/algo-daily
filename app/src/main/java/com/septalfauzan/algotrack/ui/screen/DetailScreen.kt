package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.utils.bottomBorder

@Composable
fun DetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    attendanceId: String
) {
    val context = LocalContext.current
    val textStyle: TextStyle = MaterialTheme.typography.body1
    val mapPosition = LatLng( 37.3875,  -122.0575)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapPosition, 40f)
    }
//    val mapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail $attendanceId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { _ ->
        Column(
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Rincian", style = MaterialTheme.typography.h6)
            DetailScreenItem(
                label = "Apakah anda sedang bekerja?",
                text = "Tidak",
                textStyle = textStyle
            )
            DetailScreenItem(
                label = "Alasan tidak bekerja",
                text = "Sakit Perut",
                textStyle = textStyle
            )
            DetailScreenItem(
                label = "Timestamp",
                text = "10.10.2020",
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

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    AlgoTrackTheme() {
        Surface {
            DetailScreen(attendanceId = "id", navController = rememberNavController())
        }
    }
}
