package com.skele.pomodoro.ui.timer.state

import kotlin.time.Duration

sealed interface TimerState {
    val initialTime: Duration
    val time: Duration

    data class Ready(
        override val initialTime: Duration,
        override val time: Duration,
    ) : TimerState

    data class Running(
        override val initialTime: Duration,
        override val time: Duration,
    ) : TimerState

    data class Paused(
        override val initialTime: Duration,
        override val time: Duration,
    ) : TimerState

    data class Finished(
        override val initialTime: Duration,
        override val time: Duration = Duration.ZERO,
    ) : TimerState
}
