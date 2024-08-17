package com.skele.pomodoro.ui.timer.state

import com.skele.pomodoro.data.ExceptionMessage
import com.skele.pomodoro.data.model.TaskWithDailyRecord

sealed interface TaskTimerState {
    data object NoTask : TaskTimerState

    data class HasTask(
        val taskInfo: TaskWithDailyRecord,
        val timerState: TimerState,
        val pomodoroState: PomodoroState,
    ) : TaskTimerState

    data class Error(
        val message: ExceptionMessage,
    ) : TaskTimerState
}
