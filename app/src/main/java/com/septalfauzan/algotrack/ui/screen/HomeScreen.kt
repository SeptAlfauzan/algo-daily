package com.septalfauzan.algotrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.model.UserStats
import com.septalfauzan.algotrack.navigation.Screen
import com.septalfauzan.algotrack.ui.component.*
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(
    userId: String = "1",
    navHostController: NavHostController,
    timerState: StateFlow<Long>,
    modifier: Modifier = Modifier
) {
    val timer = System.currentTimeMillis()
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = timer

    var showAlert by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.greeting, "Pagi"),
                style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                AvatarProfile(onClick = {
                    navHostController.navigate(
                        Screen.Profile.createRoute(
                            userId
                        )
                    )
                })
            }
        }
        TimerBanner(timer = timerState.collectAsState().value, isWorkDay = true)
        VacationBanner(onCreateVacation = { showAlert = true })
        Statistic()
        AlertModalDialog(
            isShowed = showAlert,
            title = "Apakah anda yakin mengajukan cuti hari ini?",
            text = "Dengan mengajukan cuti hari ini, otomatis timer absen anda akan dinonaktifkan",
            onStateChange = { showAlert = false })
    }
}

@Composable
private fun Statistic(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.your_stats), style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight(300),
            ), color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatsCard(
                    data = UserStats(
                        description = stringResource(R.string.ontime_stats),
                        value = 9
                    ), icon = Icons.Default.CalendarToday
                )
            }
            item {
                StatsCard(
                    data = UserStats(
                        description = stringResource(R.string.late_stats),
                        value = 0
                    ), icon = Icons.Outlined.Timer
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        WeekSummaryStatsCard(80)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun HomeScreenPreview() {
    val navHostController = rememberNavController()
    AlgoTrackTheme() {
        Surface() {
            HomeScreen(userId = "", navHostController = navHostController, MutableStateFlow(100L))
        }
    }
}