package com.nareshnnk.iopopups.presentation.customviews

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.nareshnnk.iopopups.core.viewutils.AsyncPopupWindow
import com.nareshnnk.iopopups.core.viewutils.ioPopupDispatcher
import com.nareshnnk.iopopups.databinding.PopupLayoutBinding
import kotlinx.coroutines.launch

class PopupIo(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private lateinit var popupWindow: AsyncPopupWindow

    private lateinit var binding: PopupLayoutBinding

    private var count = 0

    init {
        initPopupWindow()
    }

    private fun initPopupWindow() = launchOnIoDispatcher {
        popupWindow = AsyncPopupWindow(context)
        popupWindow.dismissOnTouchOutside = false
        binding = PopupLayoutBinding.inflate(
            LayoutInflater.from(context),
            popupWindow.popupView,
            false
        ).apply {
            counter.text = count.toString()
            closeButton.setOnClickListener { dismiss() }
            decrementButton.setOnClickListener {
                count--
                counter.text = count.toString()
            }
            incrementButton.setOnClickListener {
                count++
                counter.text = count.toString()
            }
        }
        popupWindow.setContentView(binding.root)
    }

    fun show(
        parent: View,
    ) = launchOnIoDispatcher {
        count = 0
        popupWindow.showAtLocation(parent, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, 200)
    }


    fun dismiss() = launchOnIoDispatcher {
        popupWindow.dismiss()
    }

    private fun launchOnIoDispatcher(block: suspend () -> Unit) {
        lifecycleOwner.lifecycleScope.launch(ioPopupDispatcher) {
            block()
        }
    }
}