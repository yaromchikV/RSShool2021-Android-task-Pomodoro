package com.rsschool.pomodoro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.databinding.ActivityMainBinding
import androidx.lifecycle.*

class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver {

    private lateinit var binding: ActivityMainBinding

    private val timers = mutableListOf<TimerModel>()
    private val timerAdapter = TimerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ProcessLifecycleOwner.get().lifecycle.addObserver(this)

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

    private var nextId = 0

    override fun add(initTime: Long, label: String) {
        binding.clickMeImage.isVisible = false
        timers.add(TimerModel(nextId++, label, initTime, System.currentTimeMillis(), 0, false))
        timerAdapter.submitList(timers.toList())

        binding.recyclerView.smoothScrollToPosition(timers.size - 1)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())

        if (timers.size == 0)
            binding.clickMeImage.isVisible = true
    }

    override fun start(id: Int) {
        changeTimer(id, true)
    }

    override fun stop(id: Int) {
        changeTimer(id, false)
    }

    private fun changeTimer(id: Int, thisIsStart: Boolean) {
        var activeTimerIndex = timers.indexOfFirst { it.isStarted }

        fun stopRunningTimer() {
            if (activeTimerIndex != -1)
                timers[activeTimerIndex].apply {
                    operationTime += System.currentTimeMillis() - startTime
                    isStarted = false
                }
        }

        if (thisIsStart) {
            stopRunningTimer()
            val index = timers.indexOfFirst { it.id == id }
            timers[index].apply {
                isStarted = true
                startTime = System.currentTimeMillis()
            }
        } else {
            stopRunningTimer()
            activeTimerIndex = -1
        }

        timerAdapter.notifyDataSetChanged()
        timerAdapter.submitList(timers.toList())
    }

    override fun onBackPressed() {
        this.moveTaskToBack(true)
    }
}