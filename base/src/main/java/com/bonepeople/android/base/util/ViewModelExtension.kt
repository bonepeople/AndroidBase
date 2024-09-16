package com.bonepeople.android.base.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStorage.getExtra
import androidx.lifecycle.ViewModelStorage.putExtraIfAbsent
import java.io.Closeable

@Suppress("Unused")
object ViewModelExtension {
    private const val LIFECYCLE_KEY = "com.bonepeople.android.base.util.ViewModelExtension.LIFECYCLE_KEY"

    val ViewModel.lifecycleOwner: LifecycleOwner
        get() {
            val lifecycleOwner: LifecycleOwner? = getExtra(LIFECYCLE_KEY)
            return lifecycleOwner ?: putExtraIfAbsent(LIFECYCLE_KEY, LifecycleOwnerHolder())
        }

    private class LifecycleOwnerHolder : LifecycleOwner, Closeable {
        private val registry = LifecycleRegistry(this)
        private var active = true

        init {
            registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        override fun getLifecycle(): Lifecycle {
            return registry
        }

        override fun close() {
            if (active) {
                active = false
                registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }
        }
    }
}