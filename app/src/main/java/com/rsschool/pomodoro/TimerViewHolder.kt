package com.rsschool.pomodoro

import android.content.res.Resources
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
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null

    fun update(timer: TimerModel) {
        binding.timerTime.text = (timer.initTime - timer.operationTime).displayTime()

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
        binding.progressBar.setPeriod(timer.initTime)
        binding.progressBar.setCurrent(timer.operationTime)
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

        binding.startPauseButton.text = resources.getString(R.string.stop)

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timer: TimerModel) {
        countDownTimer?.cancel()

        binding.startPauseButton.text = resources.getString(R.string.start)

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private var runningTime = 0L

    private fun getCountDownTimer(timer: TimerModel): CountDownTimer {
        return object : CountDownTimer(timer.initTime, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                if (timer.isStarted) {
                    runningTime = System.currentTimeMillis() - timer.startTime + timer.operationTime
                    if (runningTime > timer.initTime)
                        onFinish()
                    else {
                        binding.timerTime.text = (timer.initTime - runningTime).displayTime()
                        binding.progressBar.setCurrent(runningTime)
                    }
                }
            }

            override fun onFinish() {
                stopTimer(timer)
                timer.apply {
                    startTime = 0
                    operationTime = 0
                    isStarted = false
                }
                binding.timerTime.text = timer.initTime.displayTime()
                binding.progressBar.setCurrent(timer.initTime)
            }
        }
    }
}