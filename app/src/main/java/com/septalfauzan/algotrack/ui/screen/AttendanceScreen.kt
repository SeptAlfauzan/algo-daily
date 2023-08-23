package com.septalfauzan.algotrack.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.navigation.Screen
import com.septalfauzan.algotrack.ui.component.ButtonType
import com.septalfauzan.algotrack.ui.component.EdtTextAttandence
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AttendanceScreen(navController: NavController) {
    var currentQuestion by remember { mutableStateOf(2) }
    var selectedAnswer by remember { mutableStateOf("") }
    var reasonNotWork by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Attendance",
                style = MaterialTheme.typography.h4,
            )
            Text(
                text = "Question $currentQuestion/2",
                style = MaterialTheme.typography.subtitle1,
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = if (currentQuestion == 1) "Apakah anda sedang bekerja?" else "Alasan anda tidak bekerja?",
                style = MaterialTheme.typography.h4,
            )
            if (currentQuestion == 1) {
                Text(
                    text = "pilih sesuai kondisi anda sekarang",
                    style = MaterialTheme.typography.h6,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedAnswer == "Yes",
                            onClick = { selectedAnswer = "Yes" },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Yes",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedAnswer == "No",
                            onClick = { selectedAnswer = "No" },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                RoundedButton(
                    onClick = { currentQuestion = 2 },
                    text = "selanjutnya",
                    modifier = Modifier.align(Alignment.End)
                )
            } else if (currentQuestion == 2) {
                EdtTextAttandence(label = "Ketik Disini", onChange = { reasonNotWork = it }, value = reasonNotWork)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RoundedButton(
                        onClick = { currentQuestion = 1 },
                        text = "sebelumnya",
                        modifier = Modifier.width(150.dp)
                    )
                    RoundedButton(
                        onClick = { navController.navigate(Screen.Success.route) },
                        text = "kirim",
                        buttonType = ButtonType.SECONDARY,
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AlgoTrackTheme() {
        Surface() {
            Column() {
                val navController = rememberNavController()
                AttendanceScreen(navController = navController)
            }
        }
    }
}