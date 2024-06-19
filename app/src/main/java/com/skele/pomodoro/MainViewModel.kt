package com.skele.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skele.pomodoro.data.TaskRepository
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.service.TimerService
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = TaskRepository.instance

    var isServiceReady by mutableStateOf(false)
    var timerService by mutableStateOf<TimerService?>(null)

    var currentId = 0L
    var currentTask : TaskWithDailyRecord? by mutableStateOf(null)
    val taskList = repository.getAllTaskWithDailyRecord(Date())

    fun loadHighestPriority(){
        viewModelScope.launch{
            currentTask = repository.getHighestPriorityTaskWithDailyRecord(Date())
        }
    }
}