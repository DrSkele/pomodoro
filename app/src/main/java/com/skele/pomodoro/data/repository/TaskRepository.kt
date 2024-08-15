package com.skele.pomodoro.data.repository

import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskRecord
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getAllTaskWithDailyRecord(): Flow<List<TaskWithDailyRecord>>

    suspend fun getHighestPriorityTaskWithDailyRecord(): TaskWithDailyRecord

    suspend fun getTaskWithDailyRecord(id: Long): TaskWithDailyRecord

    suspend fun selectTaskWithId(taskId: Long): Task

    suspend fun insertOrUpdateTask(task: Task)

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun saveRecord(record: TaskRecord)
}
