package com.skele.pomodoro.ui.timer.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class TimerRepository
    @Inject
    constructor() {
        private val _timerState = MutableStateFlow<TimerState>(TimerState.Ready(Duration.ZERO))
        val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

        private var initialTime: Duration = Duration.ZERO
        private var timer: Job? = null

        fun setTimer(time: Duration) {
            initialTime = time
            _timerState.value = TimerState.Ready(time)
        }

        fun pause() {
            timer?.cancel(cause = CancellationException(TimerStopReason.Paused.message))
        }

        fun start() {
            timer?.cancel(cause = CancellationException(TimerStopReason.Started.message))
            timer = timerLoop()
        }

        fun stop() {
            timer?.cancel(cause = CancellationException(TimerStopReason.Stopped.message))
        }

        private fun timerLoop(): Job =
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    while (_timerState.value !is TimerState.Paused && _timerState.value.time > Duration.ZERO) {
                        delay(10)
                        _timerState.value =
                            TimerState.Running(_timerState.value.time.minus(10.milliseconds))
                    }

                    if (_timerState.value.time == Duration.ZERO) {
                        _timerState.value = TimerState.Finished(_timerState.value.time)
                    }
                } catch (e: CancellationException) {
                    e.message?.let { message ->
                        val reason = TimerStopReason.valueOf(message)
                        when (reason) {
                            TimerStopReason.Paused ->
                                _timerState.value =
                                    TimerState.Paused(_timerState.value.time)

                            TimerStopReason.Stopped ->
                                _timerState.value =
                                    TimerState.Ready(_timerState.value.time)

                            TimerStopReason.Started -> {}
                        }
                    }
                }
            }
    }

enum class TimerStopReason(
    val message: String,
) {
    Paused("Paused"),
    Started("Started"),
    Stopped("Stopped"),
}
