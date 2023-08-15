package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.data.AttendanceAnswer
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun QuestionItem(
    answer: String,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colors.primary else Color.Transparent
        )
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colors.primary
                )
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(text = answer)
        }
    }
}


@Composable
fun QuestionsRadioButton(answers: List<AttendanceAnswer>, modifier: Modifier = Modifier) {
    var selectedIndex: Int? by rememberSaveable {
        mutableStateOf(null)
    }
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(answers) { index, item ->
            QuestionItem(
                answer = item.answer,
                selected = selectedIndex == index
            ) { selectedIndex = index }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AlgoTrackTheme() {
        Surface() {
            QuestionsRadioButton(answers = listOf(
                AttendanceAnswer(answer = "Ya", value = true),
                AttendanceAnswer(answer = "Tidak", value = false),
            ))
        }
    }
} 
