package com.skele.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.skele.pomodoro.data.TaskRepository
import com.skele.pomodoro.data.model.TaskRecord
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.data.model.TimerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

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
        if(currentTask == null){
            currentTask = repository.getTaskWithDailyRecord(1)
        }
    }
    private fun saveTask(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveRecord(TaskRecord(
                taskId = currentTask!!.task.id,
                cnt = 1,
                dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            ))
        }
    }

    fun proceedToNextTimer() : Duration {
        if(currentTimerType == TimerType.POMODORO) saveTask()

        currentTimerType = when (currentTimerType) {
            TimerType.POMODORO -> if((currentTask!!.done+1) % 4 == 0) TimerType.LONG_BREAK else TimerType.SHORT_BREAK
            TimerType.SHORT_BREAK -> TimerType.POMODORO
            TimerType.LONG_BREAK -> TimerType.POMODORO
        }

        return currentTask!!.task.getTimeOfType(currentTimerType)
    }
}