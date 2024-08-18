package com.skele.pomodoro.ui.task.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.task.input.layout.MultiLineInputField
import com.skele.pomodoro.ui.theme.PomodoroTheme
import com.skele.pomodoro.ui.timer.state.UserAction
import kotlin.time.Duration.Companion.minutes

@Composable
fun TaskInputScreen(
    modifier: Modifier = Modifier,
    task: Task? = null,
) {
    val viewModel: TaskInputViewModel = hiltViewModel()

    TaskInputScreenContent(
        task = task,
        onAction = { action ->
            when (action) {
                is TaskInputAction.Cancel -> {}
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
fun TaskInputScreenContent(
    modifier: Modifier = Modifier,
    task: Task? = null,
    onAction: (UserAction) -> Unit = {},
) {
    val name = rememberTextFieldState(task?.description ?: "")

    val workTime =
        rememberTextFieldState(((task?.workTime?.inWholeMinutes?.rem(60)) ?: 25).toString())

    val breakTime =
        rememberTextFieldState(((task?.breakTime?.inWholeMinutes?.rem(60)) ?: 25).toString())
    val longBreak =
        rememberTextFieldState(((task?.longBreakTime?.inWholeMinutes?.rem(60)) ?: 25).toString())

    val breakInterval = rememberTextFieldState((task?.breakInterval ?: 4).toString())
    val dailyGoal = rememberTextFieldState((task?.dailyGoal ?: 5).toString())

    val red = rememberTextFieldState(((task?.color?.red?.times(255))?.toInt() ?: 255).toString())
    val green =
        rememberTextFieldState(((task?.color?.green?.times(255))?.toInt() ?: 255).toString())
    val blue =
        rememberTextFieldState(((task?.color?.blue?.times(255))?.toInt() ?: 255).toString())

    fun inputTask(): Task =
        task?.copy(
            description = name.text.toString(),
            workTimeInMillisec =
                workTime.text
                    .toString()
                    .toInt()
                    .minutes.inWholeMilliseconds,
            breakTimeInMillisec =
                breakTime.text
                    .toString()
                    .toInt()
                    .minutes.inWholeMilliseconds,
            longBreakTimeInMillisec =
                longBreak.text
                    .toString()
                    .toInt()
                    .minutes.inWholeMilliseconds,
            breakInterval = breakInterval.text.toString().toInt(),
            dailyGoal = dailyGoal.text.toString().toInt(),
            colorNum =
                Color(
                    red.text.toString().toInt(),
                    green.text.toString().toInt(),
                    blue.text.toString().toInt(),
                ).toArgb(),
        ) ?: Task(
            description = name.text.toString(),
            workTime =
                workTime.text
                    .toString()
                    .toInt()
                    .minutes,
            breakTime =
                breakTime.text
                    .toString()
                    .toInt()
                    .minutes,
            breakInterval = breakInterval.text.toString().toInt(),
            longBreakTime =
                longBreak.text
                    .toString()
                    .toInt()
                    .minutes,
            dailyGoal = dailyGoal.text.toString().toInt(),
            color =
                Color(
                    red.text.toString().toInt(),
                    green.text.toString().toInt(),
                    blue.text.toString().toInt(),
                ),
        )

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
    ) {
        MultiLineInputField(
            title = "작업 이름",
            hint = listOf("이름"),
            state = listOf(name),
        )
        MultiLineInputField(
            title = "시간 입력",
            hint = listOf("작업", "휴식", "긴 휴식"),
            state = listOf(workTime, breakTime, longBreak),
            keyboardType = KeyboardType.Number,
        )
        MultiLineInputField(
            title = "휴식 간격",
            hint = listOf("휴식 간격"),
            state = listOf(breakInterval),
            keyboardType = KeyboardType.Number,
        )
        MultiLineInputField(
            title = "일일 목표",
            hint = listOf("목표 작업량"),
            state = listOf(dailyGoal),
            keyboardType = KeyboardType.Number,
        )
        MultiLineInputField(
            title = "색상",
            hint = listOf("R", "G", "B"),
            state = listOf(red, green, blue),
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Button(onClick = { onAction(TaskInputAction.Cancel) }) {
                Text("취소")
            }
            Button(onClick = { onAction(TaskInputAction.Submit(inputTask())) }) {
                Text("확인")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TaskInputScreenPreview() {
    PomodoroTheme {
        TaskInputScreenContent()
    }
}
