package com.rsschool.pomodoro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var binding: ActivityMainBinding

    private val timers = mutableListOf<Timer>()
    private val timerAdapter = TimerAdapter(this)
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        binding.addNewTimerButton.setOnClickListener {
            if (timers.size < 20)
                SetTimeDialog(this)
            else
                Toast.makeText(applicationContext, "Too many timers", Toast.LENGTH_SHORT).show()
        }
    }

    override fun add(startTime: Long) {
        timers.add(Timer(nextId++, startTime, false))
        timerAdapter.submitList(timers.toList())
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    override fun start(id: Int, startTime: Long) {
        changeTimer(id, startTime, true)
    }

    override fun stop(id: Int, startTime: Long) {
        changeTimer(id, startTime, false)
    }

    private fun changeTimer(id: Int, startTime: Long?, isStarted: Boolean) {
        if (isStarted) {
            timers.forEachIndexed { i, it ->
                if (it.id == id) {
                    timers[i] = Timer(id, startTime ?: timers[i].startTime, true)
                } else
                    timers[i] = Timer(it.id, it.startTime, false)
            }
        } else {
            val index = timers.indexOfFirst { it.id == id }
            timers[index] = Timer(id, startTime ?: timers[index].startTime, isStarted)
        }
        timerAdapter.submitList(timers.toList())
    }
}