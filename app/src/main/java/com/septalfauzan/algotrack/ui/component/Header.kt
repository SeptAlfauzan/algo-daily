package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R

/**
 * @param painter used to give painter value of drawable image
 */
@Composable
fun Header(painter: Painter, modifier: Modifier = Modifier){
    Box(
        modifier
            .fillMaxWidth()
            .height(262.dp)
            .clip(RoundedCornerShape(bottomStart = 88.dp))
            .background(MaterialTheme.colors.secondary)
    ) {
        Image(
            painter = painter,
            contentDescription = "login illustration",
            modifier = Modifier
                .align(Alignment.Center)
                .width(240.dp)
                .height(176.dp)
        )
    }
}
