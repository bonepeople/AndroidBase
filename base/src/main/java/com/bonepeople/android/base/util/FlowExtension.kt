package com.bonepeople.android.base.util

import android.widget.CompoundButton
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

@Suppress("Unused")
object FlowExtension {
    fun <T> Flow<T>.observeWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).onEach(action).launchIn(owner.lifecycleScope)
    }

    fun <T> StateFlow<T>.observeWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).distinctUntilChanged().conflate().onEach(action).launchIn(owner.lifecycleScope)
    }

    @ExperimentalCoroutinesApi
    fun <T> Flow<T>.observeLatestWithLifecycle(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).mapLatest(action).buffer(0).launchIn(owner.lifecycleScope)
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

    fun MutableStateFlow<Boolean>.bindCompoundButton(owner: LifecycleOwner, switch: CompoundButton, changeAction: (Boolean) -> Unit = {}) {
        var lock = false
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (!lock) {
                lock = true
                this.value = isChecked
                changeAction(isChecked)
                lock = false
            }
        }
        observeWithLifecycle(owner) {
            if (!lock) {
                lock = true
                switch.isChecked = it
                changeAction(it)
                lock = false
            }
        }
    }
}