package com.skele.pomodoro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skele.pomodoro.data.dao.TaskDao
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskRecord

@Database(entities = [Task::class, TaskRecord::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
}