package com.nareshnnk.iopopups.presentation.customviews

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.nareshnnk.iopopups.R
import com.nareshnnk.iopopups.core.viewutils.AsyncPopupWindow
import com.nareshnnk.iopopups.core.viewutils.ioPopupDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastIo(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private lateinit var popupWindow: AsyncPopupWindow

    private lateinit var toastTextView: TextView

    init {
        initPopupWindow()
    }

    private fun initPopupWindow() = launchOnIoDispatcher {
        popupWindow = AsyncPopupWindow(context)
        toastTextView = TextView(context).apply {
            setBackgroundResource(R.drawable.toast_background)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER_HORIZONTAL }
            setTextColor(context.getColor(R.color.white))
            setPadding(36)
        }
        popupWindow.setContentView(toastTextView)
    }

    fun show(
        parent: View,
        message: String,
        delay: Long = 3000
    ) = launchOnIoDispatcher {
        setMessage(message)
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, 200)
        dismiss(delay)
    }

    fun showSticky(
        parent: View,
        message: String
    ) = launchOnIoDispatcher {
        setMessage(message)
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, 200)
    }

    fun setMessage(message: String) = launchOnIoDispatcher {
        toastTextView.text = message
    }

    fun dismiss(delay: Long = 3000) = launchOnIoDispatcher {
        delay(delay)
        popupWindow.dismiss()
    }

    private fun launchOnIoDispatcher(block: suspend () -> Unit) {
        lifecycleOwner.lifecycleScope.launch(ioPopupDispatcher) {
            block()
        }
    }
}