package com.skele.pomodoro.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skele.pomodoro.data.DateConverter
import java.util.Date

@Entity(tableName = "record")
data class TaskRecord(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val taskId : Long,
    var cnt : Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")val dateTime : String
)
