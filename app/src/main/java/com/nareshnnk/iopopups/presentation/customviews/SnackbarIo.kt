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
import com.nareshnnk.iopopups.databinding.SnackbarLayoutBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnackbarIo(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private lateinit var popupWindow: AsyncPopupWindow

    private lateinit var binding: SnackbarLayoutBinding

    init {
        initPopupWindow()
    }

    private fun initPopupWindow() = launchOnIoDispatcher {
        val popupViewBinding = SnackbarLayoutBinding.inflate(LayoutInflater.from(context))
        binding = popupViewBinding
        popupWindow = AsyncPopupWindow(context).apply {
            setContentView(binding.root)
        }
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
        message: String,
        closeAction: String? = null
    ) = launchOnIoDispatcher {
        setMessage(message)
        closeAction?.let(::setCloseAction)
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, 200)
    }

    fun setMessage(message: String) = launchOnIoDispatcher {
        binding.snackbarTitle.text = message
    }

    fun dismiss(delay: Long = 3000) = launchOnIoDispatcher {
        delay(delay)
        popupWindow.dismiss()
    }

    private fun setCloseAction(actionText: String) {
        binding.snackbarAction.apply {
            text = actionText
            visibility = View.VISIBLE
            setOnClickListener {
                popupWindow.dismiss()
            }
        }
    }

    private fun launchOnIoDispatcher(block: suspend () -> Unit) {
        lifecycleOwner.lifecycleScope.launch(ioPopupDispatcher) {
            block()
        }
    }
}