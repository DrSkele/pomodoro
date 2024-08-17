package com.skele.pomodoro.ui.timer.state

interface UserAction

sealed interface TimerAction {
    data object Start : TimerAction, UserAction

    data object Pause : TimerAction, UserAction

    data object Stop : TimerAction, UserAction
}

sealed interface TaskAction {
    data object AddTask : TaskAction, UserAction

    data object OpenList : TaskAction, UserAction

    data object OpenSetting : TaskAction, UserAction
}
