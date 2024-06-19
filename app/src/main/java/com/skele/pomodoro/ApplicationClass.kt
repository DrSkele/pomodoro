package com.skele.pomodoro

import android.app.Application
import com.skele.pomodoro.data.TaskRepository

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}