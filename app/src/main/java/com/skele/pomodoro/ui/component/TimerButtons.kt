package com.skele.pomodoro.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerButtons(
    modifier: Modifier = Modifier,
    isPaused: Boolean,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onCancel: () -> Unit = {}
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)){ }
        if(isPaused){
            Button(
                modifier = Modifier
                    .width(96.dp),
                onClick = onStart
            ) {
                Text(
                    "Start",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Button(
                modifier = Modifier
                    .width(96.dp),
                onClick = onPause
            ) {
                Text(
                    "Pause",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ){
            Button(
                onClick = onCancel,
            ) {
                Icon(Icons.Default.Clear, "cancel timer")
            }
        }
    }
}