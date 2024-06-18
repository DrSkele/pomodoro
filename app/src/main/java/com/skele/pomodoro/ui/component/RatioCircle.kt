package com.skele.pomodoro.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatioCircle(
    modifier: Modifier = Modifier,
    ratio : Float,
    color : Color,
    strokeSize: Dp = 5.dp
){
    val stroke = with(LocalDensity.current) { Stroke(strokeSize.toPx()) }
    val startAngle = -90 + (1-ratio) * 360f
    val degree = ratio * 360f

    Canvas(modifier) {
        val radius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2f
        val topLeft = Offset(
            x = halfSize.width - radius,
            y = halfSize.height - radius
        )
        val size = Size(radius * 2, radius * 2)
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = degree,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
    }
}

@Preview
@Composable
fun RatioCirclePreview(){
    Surface {
        RatioCircle(
            modifier = Modifier.height(300.dp).fillMaxWidth(),
            ratio = 1f,
            color = Color.Black
        )
    }
}