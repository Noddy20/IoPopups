package com.nareshnnk.iopopups.core.viewutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.nareshnnk.iopopups.core.extensions.isMainThread
import com.nareshnnk.iopopups.core.extensions.lazyNoneSynchronized

class AsyncPopupWindow(context: Context) {

    private val windowManager by lazyNoneSynchronized {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    val popupView by lazyNoneSynchronized {
        FrameLayout(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
        }
    }
    var isShowing = false
        private set
    var dismissOnTouchOutside = true
    private var dismissListener: (() -> Unit)? = null

    init {
        if (isMainThread()) {
            throw IllegalAccessException("AsyncPopupWindow must be initialized on a background thread!")
        }
    }

    fun setContentView(view: View) {
        popupView.removeAllViews()
        popupView.addView(view)
    }

    fun setBackgroundDrawable(drawable: Drawable?) {
        popupView.background = drawable
    }

    fun setOnDismissListener(listener: () -> Unit) {
        dismissListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        if (isShowing) return

        val params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            this.gravity = gravity
            this.x = x
            this.y = y
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            format = PixelFormat.TRANSPARENT
        }

        windowManager.addView(popupView, params)
        isShowing = true

        // Crucial: Set a touch listener on the root view of the Activity
        val rootView = (parent.context as? Activity)?.window?.decorView?.rootView
        rootView?.setOnTouchListener { _, event ->
            val outRect = Rect()
            popupView.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                // Touch is outside the popup, dismiss it
                if (dismissOnTouchOutside) dismiss()
                return@setOnTouchListener true // Consume the event
            }
            false // Let the event propagate if inside the popup
        }
    }

    fun dismiss() {
        if (isShowing) {
            val rootView = (popupView.context as? Activity)?.window?.decorView?.rootView
            rootView?.setOnTouchListener(null) // Important: Remove the listener to avoid memory leaks
            windowManager.removeView(popupView)
            isShowing = false
            dismissListener?.invoke()
        }
    }
}
