package com.skele.pomodoro.navigation

import kotlinx.serialization.Serializable

@Serializable
data object TaskTimer

@Serializable
data class TaskInput(
    val taskId: Long? = null,
)
