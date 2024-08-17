package com.skele.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skele.pomodoro.service.TimerService
import javax.inject.Inject

class MainViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        var isServiceReady by mutableStateOf(false)
            private set

        private var timerService by mutableStateOf<TimerService?>(null)

        fun setService(service: TimerService) {
            timerService = service
            isServiceReady = true
        }

        fun disconnectService() {
            isServiceReady = false
        }

        fun startForegroundService() {
            timerService?.startForegroundService()
        }

        fun stopForegroundService() {
            if (isServiceReady) timerService?.stopForegroundService()
        }
    }
