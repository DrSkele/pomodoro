package com.skele.pomodoro

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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val repository: TaskRepository,
    ) : ViewModel() {
        var bottomNavDestination by mutableStateOf(TimerDestination.route)

        var isServiceReady by mutableStateOf(false)
            private set

        private var timerService by mutableStateOf<TimerService?>(null)

        var taskList = MutableStateFlow<ImmutableList<TaskWithDailyRecord>>(persistentListOf())

        init {
            viewModelScope.launch {
                repository.getAllTaskWithDailyRecord().collect { list ->
                    taskList.emit(list)
                }
            }
        }

        fun getCurrentTask(): TaskWithDailyRecord? = timerService?.taskState?.currentTask

        fun getTimerState(): TimerState = timerService!!.timerState

        fun setAsCurrentTask(taskId: Long) {
            timerService?.changeTimerTask(taskId)
        }

        fun loadCurrentTask() {
            timerService?.loadTimerTask()
        }

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

        suspend fun selectTaskWithId(taskId: Long): Task = repository.selectTaskWithId(taskId)

        fun insertOrUpdateTask(task: Task) {
            viewModelScope.launch {
                repository.insertOrUpdateTask(task)
            }
        }

        fun insertTask(task: Task) {
            viewModelScope.launch {
                repository.insertTask(task)
            }
        }

        fun updateTask(task: Task) {
            viewModelScope.launch {
                repository.updateTask(task)
            }
        }
    }
