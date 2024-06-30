package com.skele.pomodoro

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class TimerState(
    var time : Duration,
) {
    private var _timeFlow = MutableStateFlow(time)
    val timeFlow : StateFlow<Duration> = _timeFlow.asStateFlow()
    private var _isPaused = MutableStateFlow(true)
    var isPaused : StateFlow<Boolean> = _isPaused.asStateFlow()

    private var runningTimer : Job? = null

    var onTimerStart : () -> Unit = {}
    var onTimerPause : () -> Unit = {}
    var onTimerFinish : () -> Unit = {}

    fun setDuration(time : Duration){
        if(this.time == _timeFlow.value) _timeFlow.value = time
        this.time = time
    }
    fun pause() {
        _isPaused.value = true
    }
    fun start() {
        _isPaused.value = false
        startTickDown()
    }
    private fun startTickDown(){
        runningTimer?.cancel()
        runningTimer = CoroutineScope(Dispatchers.Default).launch {
            tickDown()
        }
    }
    fun stop(){
        _isPaused.value = true
        runningTimer?.cancel()
        _timeFlow.value = time
    }
    private suspend fun tickDown() {
        while(_timeFlow.value > Duration.ZERO && !isPaused.value){
            _timeFlow.value = _timeFlow.value.minus(100.milliseconds);
            delay(100)
        }
        if(_timeFlow.value.inWholeMilliseconds <= 0){
            _timeFlow.value = Duration.ZERO
            _isPaused.value = true
            onTimerFinish()
        }
    }
}