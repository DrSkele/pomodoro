package com.skele.pomodoro.data.model

import androidx.room.Embedded

data class TaskWithDailyRecord(
    @Embedded val task: Task,
    val done: Int
)
