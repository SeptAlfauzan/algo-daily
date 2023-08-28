package com.septalfauzan.algotrack.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.domain.model.UserAbsen
import com.septalfauzan.algotrack.ui.component.HistoryCard
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import java.util.*

@Composable
fun HistoryScreen(navController: NavController, historyList: List<UserAbsen>) {
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
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )

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
    ) { _ ->
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier
                    .padding(bottom = 16.dp)
                    .clickable { datePicker.show() },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "date icon")
                Text(
                    text = "12 Agustus 2023",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                )
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(historyList) { history ->
                    HistoryCard(data = history, navController = navController)
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    val historyList = listOf(
        UserAbsen(Date(), true),
        UserAbsen(Date(), false),
        UserAbsen(Date(), true)
    )
    AlgoTrackTheme() {
        Surface() {
            HistoryScreen(navController = rememberNavController(), historyList = historyList)
        }
    }
}