package com.septalfauzan.algotrack.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R


@Composable
fun BottomSheetErrorHandler(message: String, retry: () -> Unit = {}, showed: Boolean = true) {
    var showed: Boolean by remember { mutableStateOf(showed) }
    val density = LocalDensity.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = showed,
            enter = slideInVertically { with(density) { -40.dp.roundToPx() } },
            exit = slideOutVertically{ with(density) { 100.dp.roundToPx() } },
        )
        {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.capitalize(), style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(text = stringResource(R.string.retry),
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onPrimary,
                        textDecoration = TextDecoration.Underline,
                    ),
                    modifier = Modifier.clickable {
                        showed = false
                        retry()
                    })
            }
        }
    }
}