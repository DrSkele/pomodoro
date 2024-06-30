package com.skele.pomodoro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("""
        SELECT 
            task.*, 
            ifnull((
                SELECT SUM(cnt)
                FROM record 
                WHERE date(dateTime) = date('now')
                AND taskId = task.id)
            , 0) AS done 
        FROM task
    """)
    fun selectAllTaskWithDailyRecord() : Flow<List<TaskWithDailyRecord>>

    @Query("""
        SELECT 
            task.*, 
            ifnull((SELECT SUM(cnt) FROM record WHERE date(record.dateTime) = date('now')) , 0) AS done 
        FROM task
        ORDER BY task.priority LIMIT 1
    """)
    suspend fun selectHighestPriorityTaskWithDailyRecord() : TaskWithDailyRecord

    @Query("""
        SELECT 
            task.*, 
            ifnull((
                SELECT SUM(cnt) 
                FROM record 
                WHERE taskId = task.id 
                AND date(dateTime) = date('now'))
            , 0) AS done 
        FROM task
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun selectTaskWithDailyRecord(id: Long) : TaskWithDailyRecord

    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun selectTaskWithId(taskId: Long) : Task

    @Insert
    suspend fun insertNewTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTask(task: Task) : Long

    @Transaction
    suspend fun insertOrUpdateTask(task: Task){
        val row = insertOrIgnoreTask(task)
        if(row < 0) updateTask(task)
    }

    @Update
    suspend fun updateTask(task: Task)

}