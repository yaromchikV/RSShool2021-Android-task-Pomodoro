package com.rsschool.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.pomodoro.databinding.TimerItemBinding

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) : RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null
    private var currentTimeIndicator = 0L

    fun bindTo(timer: TimerModel) {
        binding.timerTime.text = timer.currentMs.displayTime()

        if (timer.isStarted)
            startTimer(timer)
        else
            stopTimer(timer)

        binding.timeIndicator.setPeriod(timer.initMs)
        binding.timeIndicator.setCurrent(timer.initMs - timer.currentMs)

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: TimerModel) {
        binding.startPauseButton.setOnClickListener {
            if (timer.isStarted)
                listener.stop(timer.id, timer.currentMs, timer.initMs)
            else
                listener.start(timer.id, timer.currentMs, timer.initMs)
        }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }
    }

    private fun startTimer(timer: TimerModel) {
        binding.startPauseButton.text = "Stop"

        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timer: TimerModel) {
        binding.startPauseButton.text = "Start"

        countDownTimer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timer: TimerModel): CountDownTimer {
        return object : CountDownTimer(timer.initMs, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                if (timer.currentMs - INTERVAL < 0)
                    onFinish()

                timer.currentMs -= INTERVAL
                binding.timerTime.text = timer.currentMs.displayTime()

                currentTimeIndicator += INTERVAL
                binding.timeIndicator.setCurrent(currentTimeIndicator)
            }

            override fun onFinish() {
                stopTimer(timer)

                binding.timeIndicator.setCurrent(0)
                binding.startPauseButton.text = "Repeat"
                binding.timerTime.text = timer.initMs.displayTime()

                timer.isStarted = false
                timer.currentMs = timer.initMs
            }
        }
    }

    private fun Long.displayTime(): String {
        return if (this <= 0L) START_TIME
        else "%02d:%02d:%02d".format(
            this / 1000 / 3600,
            this / 1000 % 3600 / 60,
            this / 1000 % 60
        )
    }

    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 100L
    }
}