package com.skele.pomodoro.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerClock(
    modifier: Modifier = Modifier,
    ratio : Float,
    title : String,
    subTitle : String,
    color: Color
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ){
        RatioCircle(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            ratio = ratio,
            color = color
        )
        Column (
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                title,
                fontSize = 64.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                subTitle,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun TimerClockPreview(){
    TimerClock(
        ratio = 0.75f,
        title = "1:25:00",
        subTitle = "오늘 진행횟수 : 0/5",
        color = Color.Cyan
    )
}