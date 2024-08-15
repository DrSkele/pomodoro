package com.skele.pomodoro.ui.timer

import androidx.lifecycle.ViewModel
import com.skele.pomodoro.ui.timer.state.TimerIntent
import com.skele.pomodoro.ui.timer.state.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TimerScreenViewModel
    @Inject
    constructor(
        private val timerRepository: TimerRepository,
    ) : ViewModel() {
        private val _taskState = MutableStateFlow<TimerScreenState>(TimerScreenState.NoTask)
        val taskState: StateFlow<TimerScreenState> = _taskState.asStateFlow()

        private val _timerState = timerRepository.timerState



        fun processTimerIntent(intent: TimerIntent) {
            when (intent) {
                TimerIntent.Start -> timerRepository.start()
                TimerIntent.Pause -> timerRepository.pause()
                TimerIntent.Stop -> timerRepository.stop()
            }
        }
    }
