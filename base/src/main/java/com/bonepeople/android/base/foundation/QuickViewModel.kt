package com.bonepeople.android.base.foundation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonepeople.android.base.util.CoroutineExtension.launchOnDefault
import java.util.concurrent.atomic.AtomicBoolean

abstract class QuickViewModel : ViewModel() {
    private val initialized = AtomicBoolean(false)

    protected fun initialize(block: suspend () -> Unit) {
        if (!initialized.compareAndSet(false, true)) return

        viewModelScope.launchOnDefault {
            block()
        }
    }
}