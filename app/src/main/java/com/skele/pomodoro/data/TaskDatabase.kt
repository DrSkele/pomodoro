package com.skele.pomodoro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.skele.pomodoro.data.converter.ColorConverter
import com.skele.pomodoro.data.converter.DurationConverter
import com.skele.pomodoro.data.dao.TaskDao
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskRecord
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.ui.theme.Primary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

@Database(entities = [Task::class, TaskRecord::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao

    companion object {
        private var instance : TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) : TaskDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "pomodoro.db"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val taskDao = getInstance(context).taskDao()
                        val defaultTask = Task("Default", 25.minutes, 5.minutes, 15.minutes, 5, Primary)
                        taskDao.insertNewTask(defaultTask)
                    }
                }
            }).build()
        }
    }
}