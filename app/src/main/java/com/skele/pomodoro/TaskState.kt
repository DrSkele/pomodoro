package com.skele.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.skele.pomodoro.data.TaskRepository
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.data.model.TimerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskState {

    private val repository = TaskRepository.instance
    var currentTimerType : TimerType by mutableStateOf(TimerType.POMODORO)
        private set
    var currentTask : TaskWithDailyRecord? by mutableStateOf(null)
        private set

    suspend fun setAsCurrentTask(taskId: Long){
        currentTask = repository.getTaskWithDailyRecord(taskId)
    }
    suspend fun loadCurrentTask(){
        currentTask = currentTask?.task?.let { repository.getTaskWithDailyRecord(it.id) }
            ?: repository.getHighestPriorityTaskWithDailyRecord()
    }

    suspend fun selectTaskWithId(taskId : Long): Task {
        return repository.selectTaskWithId(taskId)
    }

    fun insertOrUpdateTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertOrUpdateTask(task)
        }
    }
    fun insertTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertTask(task)
        }
    }
    fun updateTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateTask(task)
        }
    }
}