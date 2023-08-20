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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.model.UserStats
import com.septalfauzan.algotrack.ui.component.*
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(
    logout: () -> Unit,
    timerState: StateFlow<Long>,
    modifier: Modifier = Modifier
) {
    val timer = System.currentTimeMillis()
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = timer

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {


        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Good morning", style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                AvatarProfile()
            }
        }
        TimerBanner(timer = timerState.collectAsState().value, isWorkDay = true)
        VacationBanner()
        Statistic()
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
        LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)){
            item{
                StatsCard(data = UserStats(description = "Tepat waktu hari ini", value = 9), icon = Icons.Default.CalendarToday)
            }
            item{
                StatsCard(data = UserStats(description = "Telat hari ini", value = 0), icon = Icons.Outlined.Timer)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        WeekSummaryStatsCard()
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun HomeScreenPreview() {
    AlgoTrackTheme() {
        Surface() {
            HomeScreen(logout = { /*TODO*/ }, MutableStateFlow(100L))
        }
    }
}