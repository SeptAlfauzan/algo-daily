package com.septalfauzan.algotrack.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.data.model.UserAbsen
import com.septalfauzan.algotrack.ui.component.HistoryCard
import okhttp3.internal.wait
import java.util.Date

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HistoryScreen(navController: NavController, historyList: List<UserAbsen>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "History") },
                actions = {
                    IconButton(onClick = { /* Handle sorting action */ }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                    }
                },
                backgroundColor = Color.White
            )
        }
    ) {
        LazyColumn {
            items(historyList) { history ->
                HistoryCard(data = history)
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
    HistoryScreen(navController = rememberNavController(), historyList = historyList)
}