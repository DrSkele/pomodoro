package com.skele.pomodoro.data.converter

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter

class ColorConverter {
    @TypeConverter
    fun fromInt(value: Int?) : Color? {
        return value?.let { Color(it) }
    }
    @TypeConverter
    fun fromColor(color: Color?) : Int? {
        return color?.toArgb()
    }
}