package com.septalfauzan.algotrack.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponseData
import com.septalfauzan.algotrack.domain.model.apiResponse.toAttendanceEntity
import com.septalfauzan.algotrack.helper.formatCalendarDate
import com.septalfauzan.algotrack.helper.reverseFormatCalendarDate
import com.septalfauzan.algotrack.ui.component.ErrorHandler
import com.septalfauzan.algotrack.ui.component.HistoryCard
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.utils.shimmer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

@Composable
fun AttendanceHistoryScreen(
    navController: NavController,
    getHistory: (String) -> Unit,
    reloadHistory: () -> Unit,
    historyUiState: StateFlow<UiState<List<AttendanceEntity>>>
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("") }

// Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        R.style.Theme_AlgoTrack_DatePicker,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val date = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            selectedDateText = date.formatCalendarDate()
            reloadHistory()
        }, year, month, dayOfMonth
    )

    LaunchedEffect(Unit) {
        selectedDateText = "$dayOfMonth/${month + 1}/$year".formatCalendarDate()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "History") },
                actions = {
                    IconButton(onClick = { /* Handle sorting action */ }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                    }
                },
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background
            )
        }
    ) { paddingValues ->
        historyUiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    ShimmerLoading(showShimmer = true, modifier = Modifier.padding(paddingValues))
                    getHistory(selectedDateText.reverseFormatCalendarDate())
                }
                is UiState.Success -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            Modifier
                                .padding(bottom = 16.dp)
                                .clickable { datePicker.show() },
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "date icon"
                            )
                            Text(
                                text = selectedDateText,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (uiState.data.isEmpty()) {
                                item { NoAttendanceData() }
                            } else {
                                items(uiState.data) { history ->
                                    HistoryCard(data = history, navController = navController)
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> ErrorHandler(
                    reload = { reloadHistory() },
                    errorMessage = uiState.errorMessage
                )
            }
        }
    }
}

@Composable
private fun NoAttendanceData(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_attendance_data),
            contentDescription = stringResource(
                R.string.no_attendance_data_image
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_attendance_data),
            style = MaterialTheme.typography.body1.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun ShimmerLoading(showShimmer: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .width(240.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(true)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(showShimmer = showShimmer),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(showShimmer = showShimmer),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(showShimmer = true),
        )
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    val historyList = listOf(
        AttendanceResponseData(
            id = "",
            timestamp = Date().toString(),
            createdAt = Date().toString(),
            reason = "",
            status = AttendanceStatus.ON_DUTY.toString(),
            longitude = 0.0,
            latitude = 0.0
        ),
        AttendanceResponseData(
            id = "",
            timestamp = Date().toString(),
            createdAt = Date().toString(),
            reason = "",
            status = AttendanceStatus.ON_DUTY.toString(),
            longitude = 0.0,
            latitude = 0.0
        ),
    )
    AlgoTrackTheme() {
        Surface() {
            AttendanceHistoryScreen(
                navController = rememberNavController(),
                getHistory = {},
                reloadHistory = {},
                historyUiState = MutableStateFlow(UiState.Success(historyList.map { it.toAttendanceEntity() }))
            )
        }
    }
}