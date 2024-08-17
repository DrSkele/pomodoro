package com.skele.pomodoro.ui.timer

import androidx.lifecycle.ViewModel
import com.skele.pomodoro.ui.timer.state.TaskTimerStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerScreenViewModel
    @Inject
    constructor(
        private val taskTimerStateManager: TaskTimerStateManager,
    ) : ViewModel() {

        val taskTimerState = taskTimerStateManager.taskTimerState


    }
