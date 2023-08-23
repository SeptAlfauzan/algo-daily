package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun HistoryCardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.caption.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AlgoTrackTheme() {
        Surface() {
            Column() {
                HistoryCardButton("detail", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
