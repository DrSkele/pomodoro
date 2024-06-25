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
import com.skele.pomodoro.service.TimerService
import kotlinx.coroutines.launch

class MainViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = TaskRepository.instance

    var isServiceReady by mutableStateOf(false)
    var timerService by mutableStateOf<TimerService?>(null)

    var currentId = 0L
    var currentTask : TaskWithDailyRecord? by mutableStateOf(null)
    val taskList = repository.getAllTaskWithDailyRecord()

    fun loadHighestPriority(){
        viewModelScope.launch{
            currentTask = repository.getHighestPriorityTaskWithDailyRecord()
            Log.d("TAG", "loadHighestPriority: $currentTask")
        }
    }
    suspend fun selectTaskWithId(taskId : Long): Task {
        return repository.selectTaskWithId(taskId)
    }

    fun insertOrUpdateTask(task: Task){
        viewModelScope.launch {
            repository.insertOrUpdateTask(task)
        }
    }
    fun updateTask(task: Task){
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
}