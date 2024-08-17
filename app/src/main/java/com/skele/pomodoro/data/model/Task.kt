package com.skele.pomodoro.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Immutable
@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val description: String,
    val workTimeInMillisec: Long,
    val breakTimeInMillisec: Long,
    val longBreakTimeInMillisec: Long,
    val breakInterval: Int,
    val dailyGoal: Int,
    val priority: Int,
    val colorNum: Int,
) {
    @Ignore
    val workTime = workTimeInMillisec.milliseconds

    @Ignore
    val breakTime = breakTimeInMillisec.milliseconds

    @Ignore
    val longBreakTime = longBreakTimeInMillisec.milliseconds

    @Ignore
    val color = Color(colorNum)

    constructor(
        description: String,
        workTime: Duration,
        breakTime: Duration,
        longBreakTime: Duration,
        breakInterval: Int,
        dailyGoal: Int,
        color: Color,
    ) : this(
        id = 0,
        description = description,
        workTimeInMillisec = workTime.inWholeMilliseconds,
        breakTimeInMillisec = breakTime.inWholeMilliseconds,
        longBreakTimeInMillisec = longBreakTime.inWholeMilliseconds,
        breakInterval = breakInterval,
        dailyGoal = dailyGoal,
        priority = 0,
        colorNum = color.toArgb(),
    )

    fun getTimeOfType(type: TimerType): Duration =
        when (type) {
            TimerType.POMODORO -> workTime
            TimerType.SHORT_BREAK -> breakTime
            else -> longBreakTime
        }

    companion object {
        val sampleTask: Task = Task("작업 제목입니다.", 25.minutes, 5.minutes, 15.minutes, 4, 5, Color.Cyan)
    }
}
