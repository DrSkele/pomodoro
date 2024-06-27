package com.skele.pomodoro.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id : Long,
    var description : String,
    var workTimeInMillisec : Long,
    var breakTimeInMillisec : Long,
    var longBreakTimeInMillisec : Long,
    var dailyGoal : Int,
    var priority : Int,
    var colorNum : Int,
){
    @Ignore
    val workTime = workTimeInMillisec.milliseconds
    @Ignore
    val breakTime = breakTimeInMillisec.milliseconds
    @Ignore
    val longBreakTime = longBreakTimeInMillisec.milliseconds
    @Ignore
    val color = Color(colorNum)
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
        workTimeInMillisec = workTime.inWholeMilliseconds,
        breakTimeInMillisec = breakTime.inWholeMilliseconds,
        longBreakTimeInMillisec = longBreakTime.inWholeMilliseconds,
        dailyGoal = dailyGoal,
        priority = 0,
        colorNum = color.toArgb()
    )

    fun getTimeOfType(type: TimerType) : Duration = when(type){
            TimerType.POMODORO -> workTime
            TimerType.SHORT_BREAK -> breakTime
            else -> longBreakTime
        }

    companion object{
        val sampleTask : Task = Task("작업 제목입니다.", 25.minutes, 5.minutes,15.minutes,5, Color.Cyan)
    }
}
