package com.skele.pomodoro.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skele.pomodoro.data.converter.ColorConverter
import com.skele.pomodoro.data.converter.DurationConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@TypeConverters(value = [DurationConverter::class, ColorConverter::class])
@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id : Long,
    var description : String,
    var workTime : Duration,
    var breakTime : Duration,
    var longBreakTime : Duration,
    var dailyGoal : Int,
    var priority : Int,
    var color : Color,
){
    constructor(
        description : String,
        workTime : Duration,
        breakTime : Duration,
        longBreakTime : Duration,
        dailyGoal : Int,
        color : Color,
    ) : this(
        id = 0,
        description = description,
        workTime = workTime,
        breakTime = breakTime,
        longBreakTime = longBreakTime,
        dailyGoal = dailyGoal,
        priority = 0,
        color = color
    )

    companion object{
        val sampleTask : Task = Task(0, "작업 제목입니다.", 25.minutes, 5.minutes,15.minutes,5,1, Color.Cyan)
    }
}
