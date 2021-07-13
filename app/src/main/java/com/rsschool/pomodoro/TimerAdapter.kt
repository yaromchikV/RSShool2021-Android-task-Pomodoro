package com.rsschool.pomodoro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rsschool.pomodoro.databinding.TimerItemBinding

class TimerAdapter(
    private val listener: TimerListener
) : ListAdapter<TimerModel, TimerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TimerModel>() {
            override fun areItemsTheSame(oldItem: TimerModel, newItem: TimerModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TimerModel, newItem: TimerModel): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.initMs == newItem.initMs &&
                        oldItem.isStarted == newItem.isStarted
            }

            override fun getChangePayload(oldItem: TimerModel, newItem: TimerModel) = Any()
        }
    }
}