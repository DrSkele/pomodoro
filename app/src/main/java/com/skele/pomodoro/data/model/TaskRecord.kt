package com.skele.pomodoro.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class TaskRecord(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val taskId : Long,
    var cnt : Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")val dateTime : String
){
    constructor(taskId: Long, cnt: Int) : this(0, taskId, cnt, "")
}
