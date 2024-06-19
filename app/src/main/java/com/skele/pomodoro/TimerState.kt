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
import kotlin.time.DurationUnit

class TimerState(
    val time : Duration,
) {
    private var _timeFlow = MutableStateFlow(time)
    val timeFlow : StateFlow<Duration> = _timeFlow.asStateFlow()
    private var _isPaused = MutableStateFlow(true)
    var isPaused : StateFlow<Boolean> = _isPaused.asStateFlow()

    private var runningTimer : Job? = null

    var onTimerStart : () -> Unit = {}
    var onTimerPause : () -> Unit = {}
    var onTimerFinish : () -> Unit = {}
    fun setOnTimerStart(onTimerStart : () -> Unit){
        this.onTimerStart = onTimerStart
    }
    fun setOnTimerPause(onTimerPause : () -> Unit){
        this.onTimerPause = onTimerPause
    }
    fun setOnTimerFinish(onTimerFinish : () -> Unit){
        this.onTimerFinish = onTimerFinish
    }
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
        while(_timeFlow.value > Duration.ZERO && !isPaused.value){
            _timeFlow.value = _timeFlow.value.minus(100.milliseconds);
            delay(100)
        }
        if(_timeFlow.value.inWholeMilliseconds <= 0){
            _timeFlow.value = Duration.ZERO
            onTimerFinish()
        }
    }
}