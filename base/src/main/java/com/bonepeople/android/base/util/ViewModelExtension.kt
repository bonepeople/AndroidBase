package com.bonepeople.android.base.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStorage.getExtra
import androidx.lifecycle.ViewModelStorage.putExtraIfAbsent
import androidx.lifecycle.viewModelScope
import com.bonepeople.android.base.util.CoroutineExtension.launchOnDefault
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("Unused")
object ViewModelExtension {
    private const val LIFECYCLE_KEY = "com.bonepeople.android.base.util.ViewModelExtension.LIFECYCLE_KEY"
    private const val INITIALIZE_ONCE_KEY = "com.bonepeople.android.base.util.ViewModelExtension.INITIALIZE_ONCE_KEY"

    val ViewModel.lifecycleOwner: LifecycleOwner
        get() {
            val lifecycleOwner: LifecycleOwner? = getExtra(LIFECYCLE_KEY)
            return lifecycleOwner ?: putExtraIfAbsent(LIFECYCLE_KEY, LifecycleOwnerHolder())
        }

    fun ViewModel.initializeOnce(block: suspend () -> Unit) {
        val initialized = putExtraIfAbsent(key = INITIALIZE_ONCE_KEY, value = AtomicBoolean(false))
        if (!initialized.compareAndSet(false, true)) return

        viewModelScope.launchOnDefault {
            block()
        }
    }

    private class LifecycleOwnerHolder : LifecycleOwner, Closeable {
        private val registry = LifecycleRegistry(this)
        override val lifecycle: Lifecycle = registry

        init {
            MainScope().launch {
                registry.currentState = Lifecycle.State.RESUMED
            }
        }

        override fun close() {
            registry.currentState = Lifecycle.State.DESTROYED
        }
    }
}