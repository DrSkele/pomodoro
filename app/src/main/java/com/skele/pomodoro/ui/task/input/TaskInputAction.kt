package com.skele.pomodoro.ui.task.input

import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.timer.state.UserAction

sealed interface TaskInputAction {
    data class Submit(
        val task: Task,
    ) : TaskInputAction,
        UserAction

    object Cancel : TaskInputAction, UserAction
}
