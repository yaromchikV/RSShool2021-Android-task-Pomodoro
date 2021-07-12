package com.rsschool.pomodoro

interface TimerListener {
    fun add(startTime: Long)
    fun delete(id: Int)
    fun start(id: Int, startTime: Long)
    fun stop(id: Int, startTime: Long)
}