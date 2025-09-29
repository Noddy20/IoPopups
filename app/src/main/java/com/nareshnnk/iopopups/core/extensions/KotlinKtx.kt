package com.nareshnnk.iopopups.core.extensions

fun <T> lazyNoneSynchronized(
    initializer: () -> T
): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) { initializer() }
