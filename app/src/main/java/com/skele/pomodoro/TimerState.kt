package com.skele.pomodoro

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerState(
    val time : Long,
    val onTimerFinish : () -> Unit = {}
) {
    private var _timeFlow = MutableStateFlow(time)
    val timeFlow : StateFlow<Long> = _timeFlow.asStateFlow()
    private var _isPaused = MutableStateFlow(true)
    var isPaused : StateFlow<Boolean> = _isPaused.asStateFlow()

    var runningTimer : Job? = null

    fun pause() {
        _isPaused.value = true
    }
    fun resume() {
        _isPaused.value = false
        startTimer()
    }
    fun startTimer(){
        runningTimer?.cancel()
        runningTimer = CoroutineScope(Dispatchers.Default).launch {
            tickDown()
        }
    }
    private suspend fun tickDown() {
        while(_timeFlow.value > 0 && !isPaused.value){
            _timeFlow.value--;
            delay(1000)
        }
        if(_timeFlow.value <= 0){
            onTimerFinish()
        }
    }
}