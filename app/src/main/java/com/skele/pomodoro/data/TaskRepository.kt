package com.skele.pomodoro.data

import android.content.Context
import android.util.Log
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskRecord
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow

class TaskRepository private constructor(context: Context) {

    private var database: TaskDatabase = TaskDatabase.getInstance(context)

    private val taskDao = database.taskDao()
    private val recordDao = database.recordDao()

    fun getAllTaskWithDailyRecord() : Flow<List<TaskWithDailyRecord>> {
        return taskDao.selectAllTaskWithDailyRecord()//date.time)
    }
    suspend fun getHighestPriorityTaskWithDailyRecord() : TaskWithDailyRecord {
        return taskDao.selectHighestPriorityTaskWithDailyRecord()
    }

    suspend fun getTaskWithDailyRecord(id: Long) : TaskWithDailyRecord {
        Log.d("TAG", "getTaskWithDailyRecord: $id")
        return taskDao.selectTaskWithDailyRecord(id)
    }

    suspend fun selectTaskWithId(taskId: Long) : Task{
        return taskDao.selectTaskWithId(taskId)
    }

    suspend fun insertOrUpdateTask(task: Task){
        taskDao.insertOrUpdateTask(task)
    }
    suspend fun insertTask(task: Task){
        taskDao.insertNewTask(task)
    }
    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }
    suspend fun saveRecord(record: TaskRecord){
        recordDao.insertOrUpdateTask(record)
    }

    companion object{
        private var _instance : TaskRepository? = null
        val instance get() = _instance!!
        fun initialize(context: Context){
            if(_instance == null) {
                _instance = TaskRepository(context)
            }
        }
    }
}