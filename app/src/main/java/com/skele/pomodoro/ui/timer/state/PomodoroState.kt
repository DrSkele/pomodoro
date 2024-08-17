package com.skele.pomodoro.ui.timer.state

sealed interface PomodoroState {
    val count: Int

    fun getNext(interval: Int): PomodoroState

    data class Working(
        override val count: Int,
    ) : PomodoroState {
        override fun getNext(interval: Int): PomodoroState =
            when ((count + 1) % interval) {
                0 -> LongBreak(count + 1)
                else -> ShortBreak(count + 1)
            }
    }

    data class ShortBreak(
        override val count: Int,
    ) : PomodoroState {
        override fun getNext(interval: Int): PomodoroState = PomodoroState.Working(count)
    }

    data class LongBreak(
        override val count: Int,
    ) : PomodoroState {
        override fun getNext(interval: Int): PomodoroState = PomodoroState.Working(count)
    }
}
