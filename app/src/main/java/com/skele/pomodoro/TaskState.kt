package com.skele.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.model.TimerType

class TaskState(
    task: Task,
    done: Int
) {
    var task by mutableStateOf(task)
        private set
    var done by mutableStateOf(done)
        private set
}