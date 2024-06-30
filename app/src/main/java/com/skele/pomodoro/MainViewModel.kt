package com.skele.pomodoro

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skele.pomodoro.data.TaskRepository
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.data.model.TimerType
import com.skele.pomodoro.service.TimerService
import kotlinx.coroutines.launch

class MainViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = TaskRepository.instance

    var bottomNavDestination by mutableStateOf(TimerDestination.route)

    var isServiceReady by mutableStateOf(false)
        private set

    private var timerService by mutableStateOf<TimerService?>(null)

    val taskList = repository.getAllTaskWithDailyRecord()

    fun getCurrentTask() : TaskWithDailyRecord? = timerService?.taskState?.currentTask
    fun getTimerState() : TimerState = timerService!!.timerState
    fun setAsCurrentTask(taskId: Long){
        timerService?.changeTimerTask(taskId)
    }
    fun loadCurrentTask(){
        timerService?.loadTimerTask()
    }
    fun setService(service: TimerService){
        timerService = service
        isServiceReady = true
    }
    fun disconnectService(){
        isServiceReady = false
    }
    fun startForegroundService(){
        timerService?.startForegroundService()
    }
    fun stopForegroundService(){
        if(isServiceReady) timerService?.stopForegroundService()
    }
    suspend fun selectTaskWithId(taskId : Long): Task {
        return repository.selectTaskWithId(taskId)
    }

    fun insertOrUpdateTask(task: Task){
        viewModelScope.launch {
            repository.insertOrUpdateTask(task)
        }
    }
    fun insertTask(task: Task){
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
    fun updateTask(task: Task){
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
}