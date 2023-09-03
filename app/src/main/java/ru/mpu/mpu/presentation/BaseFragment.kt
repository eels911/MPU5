package ru.mpu.lk.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment(@LayoutRes res: Int) : Fragment(res) {
    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as? KeyboardManager)?.hideKeyboard()
    }
}