package com.nareshnnk.iopopups.core.viewutils

import android.os.Handler
import android.os.HandlerThread
import com.nareshnnk.iopopups.core.extensions.lazyNoneSynchronized
import kotlinx.coroutines.android.asCoroutineDispatcher

val ioPopupThread by lazyNoneSynchronized {
    HandlerThread("async_view_thread").also { thread ->
        thread.start()
    }
}

val ioPopupDispatcher by lazyNoneSynchronized {
    Handler(ioPopupThread.looper).asCoroutineDispatcher("async_view_dispatcher")
}
