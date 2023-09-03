package ru.mpu.lk.login.presentation

import android.widget.EditText

interface KeyboardManager {
    fun hideKeyboard()
    fun showKeyboard(editText: EditText)
}
