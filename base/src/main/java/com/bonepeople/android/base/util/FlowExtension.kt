package com.bonepeople.android.base.util

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bonepeople.android.widget.util.AppLog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("Unused")
object FlowExtension {
    fun <T> Flow<T>.observeWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).conflate().onEach(action).launchIn(owner.lifecycleScope)
    }

    fun <T> StateFlow<T>.observeWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).distinctUntilChanged().conflate().onEach(action).launchIn(owner.lifecycleScope)
    }

    fun <T> StateFlow<T>.newObserveWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {

        return callbackFlow {
            owner.lifecycle.repeatOnLifecycle(activeState) {
                AppLog.defaultLog.warn("start collect")
                this@newObserveWithLifecycle.collect {
                    AppLog.defaultLog.warn("resend $it")
                    send(it)
                }
                AppLog.defaultLog.warn("stop collect")
            }
            close()
        }.distinctUntilChanged().conflate().onEach(action).launchIn(owner.lifecycleScope)
    }

    fun <T> Flow<T>.observeLatestWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return owner.lifecycleScope.launch {
            flowWithLifecycle(owner.lifecycle, activeState).collectLatest(action)
        }
    }

    @ExperimentalCoroutinesApi
    fun <T> StateFlow<T>.observeLatestWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).distinctUntilChanged().mapLatest(action).buffer(0).launchIn(owner.lifecycleScope)
    }

    fun MutableStateFlow<String>.bindEditTextValue(owner: LifecycleOwner, editText: EditText) {
        var lock = false
        editText.doAfterTextChanged {
            if (!lock) {
                lock = true
                this.value = it.toString()
                lock = false
            }
        }
        observeWithLifecycle(owner) {
            if (!lock) {
                lock = true
                editText.setText(it)
                lock = false
            }
        }
    }
}