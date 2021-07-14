package com.rsschool.pomodoro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.databinding.ActivityMainBinding
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

    override fun add(initMs: Long, label: String) {
        binding.emptyText.isVisible = false
        timers.add(TimerModel(nextId++, label, initMs, initMs, false))
        timerAdapter.submitList(timers.toList())
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())

        if (timers.size == 0)
            binding.emptyText.isVisible = true
    }

    override fun start(id: Int) {
        timers.forEach { it.isStarted = it.id == id }

        timerAdapter.notifyDataSetChanged()
        timerAdapter.submitList(timers.toList())
    }

    override fun stop(id: Int) {
        val index = timers.indexOfFirst { it.id == id }
        timers[index].isStarted = false

        timerAdapter.notifyDataSetChanged()
        timerAdapter.submitList(timers.toList())
    }

    override fun onBackPressed() {
        this.moveTaskToBack(true)
    }
}