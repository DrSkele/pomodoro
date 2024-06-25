package com.skele.pomodoro.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skele.pomodoro.data.TaskRepository
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.ui.theme.PomodoroTheme
import java.util.Date

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit = {},
    onTaskSelect: (Task) -> Unit = {}
){
    val taskList by TaskRepository.instance.getAllTaskWithDailyRecord().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ListTopAppBar(
            onAdd = onAdd,
        )
        TaskList(
            taskList = taskList,
            onItemSelect = onTaskSelect
        )
    }
}

@Composable
fun ListScreenTest(
    modifier: Modifier = Modifier,
    taskList: List<TaskWithDailyRecord>
){
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ListTopAppBar()
        TaskList(taskList = taskList)
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
    taskData : TaskWithDailyRecord,
    onSelect : (Task) -> Unit = {}
){
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onSelect(taskData.task) },
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Surface(
                color = taskData.task.color,
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            ) {}
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(
                    taskData.task.description,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "오늘 진행횟수 : ${taskData.done}/${taskData.task.dailyGoal}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "${taskData.task.workTime.inWholeMinutes}:00",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    taskList: List<TaskWithDailyRecord>,
    onItemSelect : (Task) -> Unit = {}
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(taskList){task ->
            TaskListItem(
                taskData = task,
                onSelect = onItemSelect
            )
        }
    }
}

@Preview
@Composable
fun ListScreenPreview(){
    PomodoroTheme {
        ListScreenTest(
            taskList = listOf(TaskWithDailyRecord(task = Task.sampleTask, done = 0))
        )
    }
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
        TaskListItem(taskData = TaskWithDailyRecord(task = Task.sampleTask, done = 0))
    }
}

@Preview
@Composable
fun TaskListPreview(){
    PomodoroTheme {
        TaskList(taskList = listOf(TaskWithDailyRecord(task = Task.sampleTask, done = 0)))
    }
}