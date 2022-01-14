package com.bonepeople.android.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class CoroutineLifecycleObserver(private val coroutineContext: CoroutineContext) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelCoroutine(lifecycleOwner: LifecycleOwner) {
        coroutineContext.cancel()
        lifecycleOwner.lifecycle.removeObserver(this)
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