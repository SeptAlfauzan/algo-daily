package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WeekSummaryStatsCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth().height(80.dp)
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                Text("Seminggu", style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight(300),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                ))
                Text("Total tepat waktu", style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                ))
            }
            Text("80%", style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            ), modifier = Modifier.align(Alignment.Bottom))
        }
    }
}