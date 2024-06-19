package com.skele.pomodoro.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

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
}