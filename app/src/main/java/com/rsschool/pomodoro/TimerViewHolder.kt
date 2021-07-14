package com.rsschool.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.pomodoro.databinding.TimerItemBinding

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private var countDownTimer: CountDownTimer?,
    private var progressBarPosition: Long
) : RecyclerView.ViewHolder(binding.root) {

    fun update(timer: TimerModel) {
        binding.timerTime.text = timer.currentMs.displayTime()

        if (timer.isStarted)
            startTimer(timer)
        else
            stopTimer(timer)

        updateCardView(timer)
        updateCustomView(timer)
        initButtonsListeners(timer)
    }

    private fun updateCardView(timer: TimerModel) {
        val params = binding.timerTime.layoutParams as ConstraintLayout.LayoutParams
        if (timer.label == "") {
            binding.timerLabel.isVisible = false
            params.bottomToBottom = binding.timerItem.id
        } else {
            binding.timerLabel.isVisible = true
            params.bottomToTop = binding.timerLabel.id
            binding.timerLabel.text = timer.label
        }
        binding.timerTime.requestLayout()
    }

    private fun updateCustomView(timer: TimerModel) {
        binding.progressBar.setPeriod(timer.initMs)
        progressBarPosition = timer.initMs - timer.currentMs
        binding.progressBar.setCurrent(progressBarPosition)
    }

    private fun initButtonsListeners(timer: TimerModel) {
        binding.startPauseButton.setOnClickListener {
            if (timer.isStarted)
                listener.stop(timer.id)
            else
                listener.start(timer.id)
        }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }
    }

    private fun startTimer(timer: TimerModel) {
        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()

        binding.startPauseButton.text = "Stop"

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timer: TimerModel) {
        countDownTimer?.cancel()

        binding.startPauseButton.text = "Start"

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timer: TimerModel): CountDownTimer {
        return object : CountDownTimer(timer.initMs, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                timer.currentMs -= INTERVAL
                binding.timerTime.text = timer.currentMs.displayTime()

                progressBarPosition += INTERVAL
                binding.progressBar.setCurrent(progressBarPosition)

                if (timer.currentMs - INTERVAL < 0)
                    onFinish()
            }

            override fun onFinish() {
                timer.apply {
                    currentMs = timer.initMs
                    isStarted = false
                }
                binding.timerTime.text = timer.initMs.displayTime()
                stopTimer(timer)
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