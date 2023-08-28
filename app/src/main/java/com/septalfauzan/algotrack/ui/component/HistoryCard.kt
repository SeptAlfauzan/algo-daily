package com.septalfauzan.algotrack.ui.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.domain.model.UserAbsen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.MaterialTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.theme.GreenVariant
import com.septalfauzan.algotrack.ui.theme.RedAccent

@Composable
fun HistoryCard(data: UserAbsen, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = if (data.status) "Tepat Waktu" else "Belum Absen",
                    color = if (data.status) GreenVariant else RedAccent,
                    style = MaterialTheme.typography.caption
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
                    HistoryCardButton(text = "detail", onClick = { navController.navigate(Screen.Detail.createRoute("dummy_id")) })
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
            HistoryCard(data = userData, navController = rememberNavController())
        }
    }
}