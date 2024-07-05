package com.skele.pomodoro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.skele.pomodoro.data.model.TaskRecord

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(record: TaskRecord)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTask(record: TaskRecord) : Long

    @Transaction
    suspend fun insertOrUpdateTask(record: TaskRecord){
        val row = insertOrIgnoreTask(record)
        if(row < 0) updateTask(record.taskId)
    }

    @Query("""
        UPDATE record SET cnt = cnt + 1
        WHERE taskId = :taskId
    """)
    suspend fun updateTask(taskId: Long)
}