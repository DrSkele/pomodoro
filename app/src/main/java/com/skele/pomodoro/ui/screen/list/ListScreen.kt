package com.skele.pomodoro.ui.screen.list

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skele.pomodoro.TaskState
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.theme.PomodoroTheme
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
fun ListScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

    }
}

@Composable
fun ListTopAppBar(
    modifier: Modifier = Modifier,
    onAdd : () -> Unit = {},
    onMore : () -> Unit = {}
){
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Task",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onAdd) {
                Icon(Icons.Rounded.Add, "create new task")
            }
            IconButton(onClick = onMore) {
                Icon(Icons.Default.MoreVert, "options")
            }
        }
    }
}

@Composable
fun TaskListItem(
    modifier: Modifier = Modifier,
    state : TaskState
){
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .height(80.dp),
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Surface(
                color = Color(state.task.color),
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            ) {}
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(
                    state.task.description,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "오늘 진행횟수 : ${state.done}/${state.task.dailyGoal}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "${state.task.workTime.milliseconds.inWholeMinutes}:00",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun TaskList(

){

}

@Preview
@Composable
fun ListTopAppBarPreview(){
    PomodoroTheme {
        ListTopAppBar()
    }
}

@Preview
@Composable
fun TaskListItemPreview(){
    PomodoroTheme {
        TaskListItem(state = TaskState(task = Task.sampleTask, done = 0))
    }
}