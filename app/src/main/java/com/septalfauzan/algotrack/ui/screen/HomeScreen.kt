package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.domain.model.UserStats
import com.septalfauzan.algotrack.helper.getCurrentDayCycle
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.component.*
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    timerState: StateFlow<Long>,
    modifier: Modifier = Modifier,
    setOnDuty: (Boolean) -> Unit,
    getHomeStateFlow: () -> Unit,
    onDutyValue: StateFlow<Boolean>
) {
    var showAlert by remember { mutableStateOf(false) }
    val timer = System.currentTimeMillis()
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = timer
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.greeting, context.getCurrentDayCycle()),
                style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                AvatarProfile(imageUri = "", onClick = {
                    navHostController.navigate(
                        Screen.Profile.route
                    )
                })
            }
        }
        TimerBanner(timer = timerState.collectAsState().value, isWorkDay = true)
        VacationBanner(onCreateVacation = { showAlert = true })
        Statistic()
        AlertModalDialog(
            isShowed = showAlert,
            title = if (onDutyValue.collectAsState().value) stringResource(R.string.change_offduty_title_msg) else stringResource(
                R.string.change_onduty_title_msg
            ),
            text = if (onDutyValue.collectAsState().value) stringResource(R.string.change_offduty_desc_msg) else stringResource(
                R.string.change_onduty_desc_msg
            ),
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
    AlgoTrackTheme {
        Surface {
            HomeScreen(
                navHostController = navHostController,
                MutableStateFlow(100L),
                setOnDuty = { },
                getHomeStateFlow = { },
                onDutyValue = MutableStateFlow(true)
            )
        }
    }
}