package com.rsschool.pomodoro

interface TimerListener {
    fun add(initMs: Long)
    fun delete(id: Int)
    fun start(id: Int, currentMs: Long?, initMs: Long)
    fun stop(id: Int, currentMs: Long?, initMs: Long)
}