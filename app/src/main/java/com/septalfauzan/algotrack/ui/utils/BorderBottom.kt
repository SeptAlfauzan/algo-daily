package com.septalfauzan.algotrack.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * @param width is used to defined how much border width
 * @param borderColor is used to defined border color
 */
fun Modifier.bottomBorder(width: Dp, borderColor: Color): Modifier{
    return this.drawBehind {
        val borderSize = width.toPx()
        drawLine(
            color = borderColor,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = borderSize
        )
    }
}