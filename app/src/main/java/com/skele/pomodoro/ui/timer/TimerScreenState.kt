package com.skele.pomodoro.ui.timer

import com.skele.pomodoro.data.ExceptionMessage
import com.skele.pomodoro.data.model.Task

sealed interface TimerScreenState {
    data object NoTask : TimerScreenState

    data class HasTask(
        val task: Task,
    ) : TimerScreenState

    data class Error(
        val message: ExceptionMessage,
    ) : TimerScreenState
}
