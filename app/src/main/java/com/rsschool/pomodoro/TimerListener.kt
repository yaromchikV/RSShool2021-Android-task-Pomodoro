package com.rsschool.pomodoro

interface TimerListener {
    fun add(initMs: Long, label: String)
    fun delete(id: Int)
    fun start(id: Int)
    fun stop(id: Int)
}