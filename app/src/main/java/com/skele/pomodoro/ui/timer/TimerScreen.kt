package com.skele.pomodoro.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skele.pomodoro.R
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.ui.component.TimerButtons
import com.skele.pomodoro.ui.timer.layout.TaskInfoBar
import com.skele.pomodoro.ui.timer.layout.TaskTimerClock
import com.skele.pomodoro.ui.timer.state.PomodoroState
import com.skele.pomodoro.ui.timer.state.TaskAction
import com.skele.pomodoro.ui.timer.state.TaskTimerState
import com.skele.pomodoro.ui.timer.state.TimerAction
import com.skele.pomodoro.ui.timer.state.TimerState
import com.skele.pomodoro.ui.timer.state.UserAction
import kotlin.time.Duration

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerScreenViewModel = hiltViewModel(),
) {
    val taskTimerState by viewModel.taskTimerState.collectAsStateWithLifecycle()

    TimerScreenContent(modifier = modifier, taskTimerState = taskTimerState)
}

@Composable
fun TimerScreenContent(
    modifier: Modifier = Modifier,
    taskTimerState: TaskTimerState,
    onAction: (UserAction) -> Unit = {},
) {
    when (taskTimerState) {
        is TaskTimerState.NoTask ->
            NoTaskLayout(modifier = modifier, onAddTask = {
                onAction(TaskAction.AddTask)
            })

        is TaskTimerState.HasTask ->
            HasTaskLayout(
                modifier = modifier,
                taskTimerState = taskTimerState,
            )

        is TaskTimerState.Error -> {}
    }
}

@Composable
fun NoTaskLayout(
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = onAddTask) {
            Text(text = stringResource(id = R.string.add_task_button))
        }
    }
}

@Composable
fun HasTaskLayout(
    modifier: Modifier = Modifier,
    taskTimerState: TaskTimerState.HasTask,
    onAction: (UserAction) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        TaskInfoBar(
            task = taskTimerState.taskInfo.task,
            onSettingsClick = { onAction(TaskAction.OpenSetting) },
            onClick = { onAction(TaskAction.OpenList) },
        )
        Spacer(modifier = Modifier.weight(1f))
        TaskTimerClock(
            taskInfo = taskTimerState.taskInfo,
            timerState = taskTimerState.timerState,
        )
        TimerButtons(
            isPaused = taskTimerState.timerState is TimerState.Paused,
            onStart = { onAction(TimerAction.Start) },
            onPause = { onAction(TimerAction.Pause) },
            onCancel = { onAction(TimerAction.Stop) },
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimerScreenPreviewWithNoTask() {
    TimerScreenContent(taskTimerState = TaskTimerState.NoTask)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimerScreenPreviewWithTask() {
    TimerScreenContent(
        taskTimerState =
            TaskTimerState.HasTask(
                taskInfo = TaskWithDailyRecord(task = Task.sampleTask, done = 0),
                timerState = TimerState.Ready(Duration.ZERO, Duration.ZERO),
                pomodoroState = PomodoroState.Working(0),
            ),
    )
}
