package com.septalfauzan.algotrack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    logout: () -> Unit,
) {
    Column() {
        Text(text = "Home Screen")
        Button(onClick = logout) {
            Text(text = "logout")
        }
    }
}