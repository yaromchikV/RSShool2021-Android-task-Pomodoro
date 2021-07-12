package com.rsschool.pomodoro

data class Timer(
    val id: Int,
    var startTime: Long,
    var isStarted: Boolean,
)
