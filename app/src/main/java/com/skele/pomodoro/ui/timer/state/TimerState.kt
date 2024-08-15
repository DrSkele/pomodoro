package com.skele.pomodoro.ui.timer.state

import kotlin.time.Duration

sealed interface TimerState {
    val time: Duration

    data class Ready(
        override val time: Duration,
    ) : TimerState

    data class Running(
        override val time: Duration,
    ) : TimerState

    data class Paused(
        override val time: Duration,
    ) : TimerState

    data class Finished(
        override val time: Duration,
    ) : TimerState
}
