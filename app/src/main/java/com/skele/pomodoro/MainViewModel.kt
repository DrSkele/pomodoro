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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = TaskRepository.instance

    var isServiceReady by mutableStateOf(false)
        private set

    var timerService by mutableStateOf<TimerService?>(null)
        private set

    var currentTimerType : TimerType by mutableStateOf(TimerType.POMODORO)
        private set

    val taskList = repository.getAllTaskWithDailyRecord()

    private var currentTaskId = MutableStateFlow(0L)
    val currentTaskFlow = flow {
        currentTaskId.collect{id ->
            val currentTask = if(id == 0L) repository.getHighestPriorityTaskWithDailyRecord()
            else repository.getTaskWithDailyRecord(id)

            Log.d("TAG", "onflow emit: $id")
            if(currentTaskId.value != currentTask.task.id){
                timerService?.timerState?.setDuration(currentTask.task.getTimeOfType(currentTimerType))
                currentTaskId.value = currentTask.task.id
            }
            emit(currentTask)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

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