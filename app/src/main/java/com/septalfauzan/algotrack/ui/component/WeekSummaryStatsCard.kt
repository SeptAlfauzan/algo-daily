package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R

@Composable
fun WeekSummaryStatsCard(onTimeValuePercent: Int, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .height(120.dp)
                .padding(horizontal = 28.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(120.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.in_week), style = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight(300),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                    )
                )
                Text(
                    stringResource(R.string.ontime_total), style = MaterialTheme.typography.h5
                )
            }
            Text(
                stringResource(R.string.with_percent, onTimeValuePercent),
                style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colors.primary
                ),
                modifier = Modifier.weight(1f).align(Alignment.Bottom)
            )
        }
    }
}