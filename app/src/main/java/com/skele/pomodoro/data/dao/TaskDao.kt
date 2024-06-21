package com.skele.pomodoro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("""
        SELECT task.*, ifnull(record.cnt, 0) AS done FROM task
        LEFT JOIN record ON record.taskId = taskId
        WHERE date(record.dateTime) = date(:today)
    """)
    fun selectAllTaskWithDailyRecord(today: Long) : Flow<List<TaskWithDailyRecord>>

    @Query("""
        SELECT task.*, ifnull(record.cnt, 0) AS done FROM task
        LEFT JOIN record ON record.taskId = taskId
        WHERE date(record.dateTime) = date(:today)
        ORDER BY task.priority LIMIT 1
    """)
    suspend fun selectHighestPriorityTaskWithDailyRecord(today: Long) : TaskWithDailyRecord

    @Query("""
        SELECT task.*, ifnull(record.cnt, 0) AS done FROM task
        LEFT JOIN record ON record.taskId = taskId
        WHERE task.id = :id
        AND date(record.dateTime) = date(:today)
    """)
    fun selectTaskWithDailyRecord(today: Long, id: Long) : Flow<TaskWithDailyRecord>

    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun selectTaskWithId(taskId: Long)

    @Insert
    suspend fun insertNewTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

}