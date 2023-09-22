package com.septalfauzan.algotrack.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.domain.model.HomeData
import com.septalfauzan.algotrack.domain.model.UserStats
import com.septalfauzan.algotrack.domain.model.ui.UiState
import com.septalfauzan.algotrack.helper.getCurrentDayCycle
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.component.AlertModalDialog
import com.septalfauzan.algotrack.ui.component.AvatarProfile
import com.septalfauzan.algotrack.ui.component.ErrorHandler
import com.septalfauzan.algotrack.ui.component.StatsCard
import com.septalfauzan.algotrack.ui.component.TimerBanner
import com.septalfauzan.algotrack.ui.component.VacationBanner
import com.septalfauzan.algotrack.ui.component.WeekSummaryStatsCard
import com.septalfauzan.algotrack.ui.utils.shimmer
import com.septalfauzan.algotrack.util.Notification
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    timerState: StateFlow<Long>,
    modifier: Modifier = Modifier,
    setOnDuty: (Boolean) -> Unit,
    homeData: StateFlow<UiState<HomeData>>,
    getHomeStateFlow: () -> Unit,
    onDutyValue: StateFlow<Boolean>,
    reloadHomeData: () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = loading, onRefresh = { reloadHomeData() })

    val timer = System.currentTimeMillis()
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = timer
    val context = LocalContext.current

    val isWorkState = onDutyValue.collectAsState().value


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        homeData.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    loading = true
                    ShimmerLoading()
                    getHomeStateFlow()
                }
                is UiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ErrorHandler(
                            reload = reloadHomeData,
                            errorMessage = "Error: ${uiState.errorMessage}"
                        )
                    }
                }
                is UiState.Success -> {
                    loading = false
                    val homeUiStateData = uiState.data
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .statusBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(
                                    R.string.greeting,
                                    context.getCurrentDayCycle()
                                ),
                                style = MaterialTheme.typography.h4.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                AvatarProfile(
                                    imageUri = homeUiStateData.profile.data.photoUrl ?: "",
                                    onClick = {
                                        navHostController.navigate(
                                            Screen.Profile.route
                                        )
                                    })
                            }
                        }
                        TimerBanner(
                            timer = timerState.collectAsState().value,
                            onWork = (isWorkState && Notification.isWorkHour() && Notification.isWorkDay())
                        )
                        VacationBanner(action = { showAlert = true }, isWork = isWorkState)
                        homeUiStateData.stats.let {
                            Statistic(
                                ontime = it.data?.onTimeCountDay ?: 0,
                                late = it.data?.lateCountDay ?: 0,
                                weekPercentage = it.data
                                    ?.onTimePercentageWeek ?: 0
                            )
                        }
                        AlertModalDialog(
                            isShowed = showAlert,
                            title = if (isWorkState) stringResource(R.string.change_offduty_title_msg) else stringResource(
                                R.string.change_onduty_title_msg
                            ),
                            text = if (isWorkState) stringResource(R.string.change_offduty_desc_msg) else stringResource(
                                R.string.change_onduty_desc_msg
                            ),
                            onStateChange = { showAlert = false },
                            onConfirmYes = {
                                try {
                                    setOnDuty(!isWorkState)
                                    navHostController.navigate(
                                        Screen.Success.createRoute(
                                            context.getString(R.string.success_change_status),
                                            context.getString(if (!isWorkState) R.string.status_desc_work else R.string.status_desc_permit)
                                        )
                                    )
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = loading,
                state = pullRefreshState,
                modifier = Modifier.padding(top = if(loading) 32.dp else 0.dp).align(Alignment.TopCenter),
            )
        }
    }

}


@Composable
private fun Statistic(ontime: Int, late: Int, weekPercentage: Int, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                    modifier = modifier,
                    data = UserStats(
                        description = stringResource(R.string.ontime_stats),
                        value = ontime
                    ), icon = Icons.Default.CalendarToday
                )
            }
            item {
                StatsCard(
                    modifier = modifier,
                    data = UserStats(
                        description = stringResource(R.string.late_stats),
                        value = late
                    ), icon = Icons.Outlined.Timer
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        WeekSummaryStatsCard(weekPercentage)
    }
}

@Composable
private fun ShimmerLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .width(240.dp)
                        .height(58.dp)
                        .shimmer(true)
                )
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(58.dp)
                        .shimmer(true)
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .shimmer(true)
                )
            }
        }
        Box(
            modifier = Modifier
                .height(158.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .shimmer(true)
        )
        Box(
            modifier = Modifier
                .height(136.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .shimmer(true)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val boxModifier = Modifier
                .width(172.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmer(true)
            Box(modifier = boxModifier)
            Box(modifier = boxModifier)
        }
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .shimmer(true)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}