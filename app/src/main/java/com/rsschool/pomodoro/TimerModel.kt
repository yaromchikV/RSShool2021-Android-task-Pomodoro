package com.rsschool.pomodoro

data class TimerModel(
    val id: Int,
    val label: String,
    var initMs: Long,
    var currentMs: Long,
    var isStarted: Boolean
)