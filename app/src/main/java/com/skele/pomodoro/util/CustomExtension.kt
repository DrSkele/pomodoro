package com.skele.pomodoro.util

import kotlin.time.Duration

fun Duration.toMinuteFormatString() : String{
    return String.format(null, "%02d:%02d", this.inWholeMinutes, this.inWholeSeconds%60)
}