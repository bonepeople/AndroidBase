package com.bonepeople.android.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class CoroutineLifecycleObserver(private val coroutineContext: CoroutineContext) : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            coroutineContext.cancel()
            source.lifecycle.removeObserver(this)
        }
    }

    override fun hashCode(): Int {
        return coroutineContext.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is CoroutineLifecycleObserver) {
            other.coroutineContext == coroutineContext
        } else
            false
    }
}