package com.rsschool.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var binding: ActivityMainBinding

    private val timers = mutableListOf<TimerModel>()
    private val timerAdapter = TimerAdapter(this)
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
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

    override fun add(initMs: Long) {
        binding.emptyText.isVisible = false
        timers.add(TimerModel(nextId++, initMs, initMs, false))
        timerAdapter.submitList(timers.toList())
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
        if (timers.size == 0) binding.emptyText.isVisible = true
    }

    override fun start(id: Int, currentMs: Long?, initMs: Long) {
        changeTimer(id, currentMs, initMs, true)
    }

    override fun stop(id: Int, currentMs: Long?, initMs: Long) {
        changeTimer(id, currentMs, initMs, false)
    }

    private fun changeTimer(id: Int, currentMs: Long?, initMs: Long, isStarted: Boolean) {
        if (isStarted) {
            timers.forEachIndexed { i, it ->
                if (it.id == id) {
                    timers[i] = TimerModel(id, currentMs ?: timers[i].currentMs, initMs, true)
                } else
                    timers[i] = TimerModel(it.id, it.currentMs, it.initMs, false)
            }
        } else {
            val index = timers.indexOfFirst { it.id == id }
            timers[index] = TimerModel(id, currentMs ?: timers[index].currentMs, initMs, isStarted)
        }
        timerAdapter.submitList(timers.toList())
    }
}