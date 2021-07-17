package com.rsschool.pomodoro

fun Long.displayTime(): String {
    return if (this <= 0L) START_TIME
    else "%02d:%02d:%02d".format(
        this / 1000 / 3600,
        this / 1000 % 3600 / 60,
        this / 1000 % 60
    )
}

const val START_TIME = "00:00:00"
const val INTERVAL = 100L