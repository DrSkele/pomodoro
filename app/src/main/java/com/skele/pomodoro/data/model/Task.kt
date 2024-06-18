package com.skele.pomodoro.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class Task(
    val id : Long,
    var description : String,
    var workTime : Long,
    var breakTime : Long,
    var longBreakTime : Long,
    var dailyGoal : Int,
    var priority : Int,
    var color : Int,
){
    companion object{
        val sampleTask : Task = Task(1, "작업 제목입니다.", 2500000, 250000,250000,5,1, Color.Cyan.toArgb())
    }
}
