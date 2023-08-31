package com.septalfauzan.algotrack.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.septalfauzan.algotrack.presentation.AttendanceViewModel
import com.septalfauzan.algotrack.ui.component.ButtonType
import com.septalfauzan.algotrack.ui.component.EdtTextAttandence
import com.septalfauzan.algotrack.ui.component.RoundedButton

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AttendanceScreen(id: String, navController: NavController, viewModel: AttendanceViewModel) {
    var currentQuestion by remember { mutableStateOf(1) }
    var selectedAnswer by remember { mutableStateOf("") }
    var reasonNotWork by remember { mutableStateOf("") }

    val offsetQuestion1 by animateDpAsState(targetValue = if (currentQuestion == 1) 0.dp else (-1000).dp)
    val offsetQuestion2 by animateDpAsState(targetValue = if (currentQuestion == 2) 0.dp else (1000).dp)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(x = offsetQuestion1)
        ) {
            Text(
                text = "Attendance",
                style = MaterialTheme.typography.h6,
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
//                .offset(x = offset)
        ) {
            if (currentQuestion == 1) {
                Column(modifier = Modifier.offset(x = offsetQuestion1)) {
                    Text(
                        text = "Apakah anda sedang bekerja?",
                        style = MaterialTheme.typography.h5,
                    )
                    Text(
                        text = "pilih sesuai kondisi anda sekarang",
                        style = MaterialTheme.typography.h6,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                    Spacer(modifier = Modifier.weight(1f))
                    RoundedButton(
                        onClick = { currentQuestion = 2 },
                        text = "selanjutnya",
                        buttonType = ButtonType.PRIMARY,
                        modifier = Modifier.align(Alignment.End),
                        enabled = selectedAnswer.isNotBlank(),
                    )
                }
            } else if (currentQuestion == 2) {
                Column(modifier = Modifier.offset(x = offsetQuestion2)) {
                    Text(
                        text = "Alasan anda tidak bekerja?",
                        style = MaterialTheme.typography.h5,
                    )
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
                            onClick = { viewModel.updateAttendance(id, selectedAnswer, reasonNotWork, navController)},
                            text = "kirim",
                            buttonType = ButtonType.PRIMARY,
                            modifier = Modifier.width(150.dp),
                            enabled = reasonNotWork.isNotBlank(),
                        )
                    }
                }
            }
        }
    }
}