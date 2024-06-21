package com.skele.pomodoro.data

import android.content.Context
import androidx.room.Room
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TaskRepository private constructor(context: Context) {
    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        "pomodoro.db"
    ).build()

    private val dao = database.taskDao()

    fun getAllTaskWithDailyRecord(date: Date) : Flow<List<TaskWithDailyRecord>> {
        return dao.selectAllTaskWithDailyRecord(date.time)
    }

    suspend fun getHighestPriorityTaskWithDailyRecord(date: Date) : TaskWithDailyRecord {
        return dao.selectHighestPriorityTaskWithDailyRecord(date.time)
    }

    fun getTaskWithDailyRecord(date: Date, id: Long) : Flow<TaskWithDailyRecord> {
        return dao.selectTaskWithDailyRecord(date.time, id)
    }

    suspend fun selectTaskWithId(taskId: Long){
        dao.selectTaskWithId(taskId)
    }

    suspend fun insertNewTask(task: Task){
        dao.insertNewTask(task)
    }

    suspend fun updateTask(task: Task){
        dao.updateTask(task)
    }

    companion object{
        private var _instance : TaskRepository? = null
        val instance get() = _instance!!
        fun initialize(context: Context){
            if(_instance == null) _instance = TaskRepository(context)
        }
    }
}