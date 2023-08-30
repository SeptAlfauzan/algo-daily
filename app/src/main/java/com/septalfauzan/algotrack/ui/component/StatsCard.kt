package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.septalfauzan.algotrack.domain.model.UserStats
import com.septalfauzan.algotrack.ui.theme.BlueSecondary

@Composable
fun StatsCard(data: UserStats, icon: ImageVector, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        shape = shape,
        modifier = modifier
            .width(172.dp)
            .height(68.dp)
    ) {
        Row(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(BlueSecondary.copy(alpha = 0.3f))
                    .width(56.dp)
                    .height(68.dp)
                    .clip(shape), contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = "icon stats")
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)) {
                Text(
                    text = data.description, style = MaterialTheme.typography.caption.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(300)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = data.value.toString(), style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.End
                    ), modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}