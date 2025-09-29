package com.nareshnnk.iopopups.core.extensions

import android.os.Looper

fun isMainThread(): Boolean {
    // Check if the current Looper is the Main Looper
    return Looper.myLooper() == Looper.getMainLooper()
}