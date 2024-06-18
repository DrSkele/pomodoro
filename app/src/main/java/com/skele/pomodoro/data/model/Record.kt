package com.skele.pomodoro.data.model

data class Record(
    val id : Long,
    val taskId : Long,
    var cnt : Int,
    val dateTime : Long
)
