package com.skele.pomodoro.ui.screen.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skele.pomodoro.TaskState
import com.skele.pomodoro.TimerState
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.component.RatioCircle
import com.skele.pomodoro.ui.theme.PomodoroTheme
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timer : TimerState,
    taskState: TaskState,
    onSettingsClick: () -> Unit,
){
    val time by timer.timeFlow.collectAsStateWithLifecycle()
    val ratio = (time / timer.time).toFloat()
    val timeFormat = String.format(null, "%d:%2d:%2d", time.hours, time.minutes, time.seconds)
    val taskDone = "오늘 진행횟수 : ${taskState.done}/${taskState.task.dailyGoal}"

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TaskInfoBar(
            task = taskState.task,
            onSettingsClick = onSettingsClick
        )
        TimerClock(
            ratio = ratio,
            title = timeFormat,
            subTitle = taskDone,
            color = Color(taskState.task.color)
        )
    }
}
@Composable
fun TaskInfoBar(
    modifier : Modifier = Modifier,
    task: Task,
    onSettingsClick : () -> Unit = {},
){
    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        Row (
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Surface(
                color = Color(task.color),
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            ) {}
            Text(
                text = task.description,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onSettingsClick) {
                Icon(
                    Icons.Rounded.Settings,
                    "task settings",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TimerClock(
    modifier: Modifier = Modifier,
    ratio : Float,
    title : String,
    subTitle : String,
    color: Color
){
    Box(modifier = modifier){
        RatioCircle(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.Center),
            ratio = ratio,
            color = color
        )
        Column (
            modifier = Modifier
                .fillMaxWidth(),
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

@Preview
@Composable
fun TaskInfoBarPreview(){
    PomodoroTheme {
        TaskInfoBar(task = Task(1, "작업 제목입니다.", 1, 1,1,1,1, Color.Cyan.toArgb()))
    }
}

@Preview
@Composable
fun TimerClockPreview(){
    PomodoroTheme {
        TimerClock(
            ratio = 0.75f,
            title = "1:25:00",
            subTitle = "오늘 진행횟수 : 0/5",
            color = Color.Cyan,
            modifier = Modifier.height(300.dp)
        )
    }
}

@Preview
@Composable
fun TimerButtonsPreview(){
    PomodoroTheme {
        TimerButtons(
            isPaused = true
        )
    }
}