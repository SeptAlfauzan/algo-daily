package com.septalfauzan.algotrack.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun SwitchButton(isChecked: Boolean, onClick: () -> Unit) {

    val circleSize = 24.dp
    val offset by animateDpAsState(
        targetValue = if (isChecked) circleSize else 0.dp,
        animationSpec = tween(200)
    )
    val bg by animateColorAsState(
        targetValue = if (isChecked) MaterialTheme.colors.primary else MaterialTheme.colors.secondary.copy(alpha = 0.3f),
        animationSpec = tween(200)
    )

    Box(
        modifier = Modifier
            .width(circleSize * 2)
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable { onClick() }
        ,
    ) {
        Box(
            modifier = Modifier
                .offset(x = offset)
                .size(circleSize)
                .padding(2.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.background)
        )
    }
}