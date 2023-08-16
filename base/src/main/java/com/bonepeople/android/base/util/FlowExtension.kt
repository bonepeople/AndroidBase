package com.bonepeople.android.base.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("UNUSED")
object FlowExtension {
    fun <T> Flow<T>.observeWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).onEach(action).launchIn(owner.lifecycleScope)
    }
}