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

    var timerService by mutableStateOf<TimerService?>(null)
        private set

    var currentTimerType : TimerType by mutableStateOf(TimerType.POMODORO)
        private set

    val taskList = repository.getAllTaskWithDailyRecord()

    var currentTask : TaskWithDailyRecord? by mutableStateOf(null)
        private set

    fun setAsCurrentTask(task: Task){
        timerService?.timerState?.stop()
        viewModelScope.launch {
            currentTask = repository.getTaskWithDailyRecord(task.id)
            currentTask?.task?.getTimeOfType(currentTimerType)
                ?.let { timerService?.timerState?.setDuration(it) }
        }
    }
    fun loadCurrentTask(){
        viewModelScope.launch{
            currentTask = currentTask?.task?.let { repository.getTaskWithDailyRecord(it.id) }
                ?: repository.getHighestPriorityTaskWithDailyRecord()
            currentTask?.task?.getTimeOfType(currentTimerType)
                ?.let { timerService?.timerState?.setDuration(it) }
        }
    }

    fun setService(service: TimerService){
        isServiceReady = true
        timerService = service
    }
    fun disconnectService(){
        isServiceReady = false
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

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "onCleared: ")
    }
}