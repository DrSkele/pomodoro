package com.skele.pomodoro.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class TaskRecord(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val taskId : Long,
    var cnt : Int,
    @ColumnInfo(defaultValue = "('Created at' || CURRENT_TIMESTAMP)")val dateTime : String
){
    constructor(taskId: Long, cnt: Int, dateTime: String) : this(0, taskId, cnt, dateTime)
}
