package com.rsschool.pomodoro

data class TimerModel(
    val id: Int,
    var currentMs: Long,
    var initMs: Long,
    var isStarted: Boolean
)
