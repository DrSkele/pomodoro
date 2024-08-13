package com.skele.pomodoro.data.repositoryimpl

import com.skele.pomodoro.data.TaskDatabase
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskRecord
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.data.repository.TaskRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl
    @Inject
    constructor(
        private val database: TaskDatabase,
    ) : TaskRepository {
        private val taskDao = database.taskDao()
        private val recordDao = database.recordDao()

        override suspend fun getAllTaskWithDailyRecord(): Flow<ImmutableList<TaskWithDailyRecord>> = taskDao.selectAllTaskWithDailyRecord()

        override suspend fun getHighestPriorityTaskWithDailyRecord(): TaskWithDailyRecord =
            taskDao.selectHighestPriorityTaskWithDailyRecord()

        override suspend fun getTaskWithDailyRecord(id: Long): TaskWithDailyRecord = taskDao.selectTaskWithDailyRecord(id)

        override suspend fun selectTaskWithId(taskId: Long): Task = taskDao.selectTaskWithId(taskId)

        override suspend fun insertOrUpdateTask(task: Task) {
            taskDao.insertOrUpdateTask(task)
        }

        override suspend fun insertTask(task: Task) {
            taskDao.insertNewTask(task)
        }

        override suspend fun updateTask(task: Task) {
            taskDao.updateTask(task)
        }

        override suspend fun saveRecord(record: TaskRecord) {
            recordDao.insertOrUpdateTask(record)
        }
    }
