package com.septalfauzan.algotrack.ui.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AttendanceResponseData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.toAttendanceEntity
import com.septalfauzan.algotrack.helper.formatGMTtoUTC
import com.septalfauzan.algotrack.helper.formatTimeStampDatasource
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.theme.GreenVariant
import com.septalfauzan.algotrack.ui.theme.RedAccent

@Composable
fun HistoryCard(data: AttendanceEntity, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.time),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = when (data.status) {
                        AttendanceStatus.PERMIT -> stringResource(R.string.permit)
                        AttendanceStatus.ON_DUTY -> stringResource(R.string.on_duty)
                        AttendanceStatus.OFF_DUTY -> stringResource(R.string.off_duty)
                        AttendanceStatus.NOT_FILLED -> stringResource(R.string.not_filled)
                    },
                    color = if (data.status == AttendanceStatus.ON_DUTY) GreenVariant else RedAccent,
                    style = MaterialTheme.typography.caption
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column {
                        Text(
                            text = stringResource(R.string.created),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            style = MaterialTheme.typography.caption,
                        )
                        Text(
                            text = data.createdAt.formatTimeStampDatasource(),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.edited),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            style = MaterialTheme.typography.caption,
                        )
                        Text(
                            text = if (data.timestamp == data.createdAt) "-" else data.timestamp.formatTimeStampDatasource(),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                }
                TextButton(onClick = {
                    if (data.status == AttendanceStatus.NOT_FILLED) {
                        navController.navigate(
                            Screen.Attendance.createRoute(
                                data.id,
                                data.createdAt.formatGMTtoUTC()
                            )
                        )
                    } else {
                        navController.navigate(Screen.Detail.createRoute(data.id))
                    }
                }) {
                    Text(
                        text = if (data.status == AttendanceStatus.NOT_FILLED) stringResource(R.string.attend) else stringResource(
                            R.string.detail
                        ),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryCardPreview() {
    AlgoTrackTheme() {
        Surface() {
            val userData = AttendanceResponseData(
                id = "",
                timestamp = "2023-08-29T05:55:21.071Z",
                createdAt = "2023-08-29T05:55:21.071Z",
                status = AttendanceStatus.ON_DUTY.toString(),
                reason = "",
                latitude = 0.0,
                longitude = 0.0,
            )
            HistoryCard(
                data = userData.toAttendanceEntity(),
                navController = rememberNavController()
            )
        }
    }
}