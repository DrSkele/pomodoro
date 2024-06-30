package com.skele.pomodoro.data

import android.content.Context
import android.util.Log
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow

class TaskRepository private constructor(context: Context) {

    private var database: TaskDatabase = TaskDatabase.getInstance(context)

    private val dao = database.taskDao()

    fun getAllTaskWithDailyRecord() : Flow<List<TaskWithDailyRecord>> {
        return dao.selectAllTaskWithDailyRecord()//date.time)
    }
    suspend fun getHighestPriorityTaskWithDailyRecord() : TaskWithDailyRecord {
        return dao.selectHighestPriorityTaskWithDailyRecord()
    }

    suspend fun getTaskWithDailyRecord(id: Long) : TaskWithDailyRecord {
        Log.d("TAG", "getTaskWithDailyRecord: $id")
        return dao.selectTaskWithDailyRecord(id)
    }

    suspend fun selectTaskWithId(taskId: Long) : Task{
        return dao.selectTaskWithId(taskId)
    }

    suspend fun insertOrUpdateTask(task: Task){
        dao.insertOrUpdateTask(task)
    }
    suspend fun insertTask(task: Task){
        dao.insertNewTask(task)
    }
    suspend fun updateTask(task: Task){
        dao.updateTask(task)
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