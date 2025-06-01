package com.bonepeople.android.base.util

import kotlinx.coroutines.*

@Suppress("Unused")
object CoroutineExtension {
    fun CoroutineScope.launchOnMain(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Main, start, block)
    }

    fun CoroutineScope.launchOnIO(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.IO, start, block)
    }

    fun CoroutineScope.launchOnDefault(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Default, start, block)
    }
}