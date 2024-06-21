package com.skele.pomodoro.data.converter

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DurationConverter {
    @TypeConverter
    fun fromMilliseconds(value: Long?) : Duration? {
        return value?.milliseconds
    }
    @TypeConverter
    fun fromDuration(time: Duration?) : Long? {
        return time?.inWholeMilliseconds
    }
}