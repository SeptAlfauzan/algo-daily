package com.septalfauzan.algotrack.ui.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.septalfauzan.algotrack.data.model.UserAbsen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.MaterialTheme
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun HistoryCard(data: UserAbsen) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Waktu",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = if (data.status) "Tepat Waktu" else "Belum Absen",
                    color = if (data.status) Color.Green else Color.Red,
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault()).format(data.tanggal),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                if (data.status) {
                    HistoryCardButton(text = "detail", onClick = { /*TODO*/ })
                } else {
                    HistoryCardButton(text = "attend", onClick = { /*TODO*/ })
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryCardPreview() {
    AlgoTrackTheme() {
        Surface() {
            val userData = UserAbsen(
                tanggal = Date(),
                status = false
            )
            HistoryCard(data = userData)
        }
    }
}