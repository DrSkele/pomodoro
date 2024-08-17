package com.skele.pomodoro.ui.timer.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.ui.component.TimerClock
import com.skele.pomodoro.ui.timer.state.TimerState
import com.skele.pomodoro.util.toMinuteFormatString

@Composable
fun TaskTimerClock(
    modifier: Modifier = Modifier,
    taskInfo: TaskWithDailyRecord,
    timerState: TimerState,
) {
    val ratio = (timerState.time / timerState.initialTime).toFloat()
    val timeFormat = timerState.time.toMinuteFormatString()
    val taskDone = "오늘 진행횟수 : ${taskInfo.done}/${taskInfo.task.dailyGoal}"

    TimerClock(
        modifier = modifier,
        ratio = ratio,
        title = timeFormat,
        subTitle = taskDone,
        color = taskInfo.task.color,
    )
}
