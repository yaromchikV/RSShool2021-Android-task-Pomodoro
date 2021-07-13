package com.rsschool.pomodoro

import android.app.Dialog
import com.rsschool.pomodoro.databinding.DialogTimeBinding

class SetTimeDialog(private val activity: MainActivity) : Dialog(activity) {
    private val dialogBinding = DialogTimeBinding.inflate(activity.layoutInflater)

    init {
        setContentView(dialogBinding.root)

        val listOf24 = createFilledArray(24)
        val listOf60 = createFilledArray(60)

        dialogBinding.numberPickerHours.apply {
            minValue = 0
            maxValue = 23
            displayedValues = listOf24
            setOnValueChangedListener { _, _, _ -> onValueChanged() }
        }

        dialogBinding.numberPickerMinutes.apply {
            minValue = 0
            maxValue = 59
            displayedValues = listOf60
            setOnValueChangedListener { _, _, _ -> onValueChanged() }
        }

        dialogBinding.numberPickerSeconds.apply {
            minValue = 0
            maxValue = 59
            displayedValues = listOf60
            setOnValueChangedListener { _, _, _ -> onValueChanged() }
        }

        dialogBinding.buttonOk.setOnClickListener {
            val hours = dialogBinding.numberPickerHours.value
            val minutes = dialogBinding.numberPickerMinutes.value
            val seconds = dialogBinding.numberPickerSeconds.value
            val initMs = hours * 3600000L + minutes * 60000L + seconds * 1000L

            activity.add(initMs)
            dismiss()
        }

        dialogBinding.buttonCancel.setOnClickListener {
            dismiss()
        }

        show()
    }

    private fun onValueChanged() {
        dialogBinding.buttonOk.isEnabled = dialogBinding.numberPickerHours.value != 0 ||
                dialogBinding.numberPickerMinutes.value != 0 ||
                dialogBinding.numberPickerSeconds.value != 0
    }

    private fun createFilledArray(size: Int): Array<String> {
        val list = arrayListOf<String>()
        for (i in 0 until size) if (i < 10) list.add("0$i") else list.add("$i")
        return list.toTypedArray()
    }
}