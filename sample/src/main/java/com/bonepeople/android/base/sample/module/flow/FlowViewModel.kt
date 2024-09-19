package com.bonepeople.android.base.sample.module.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonepeople.android.base.util.CoroutineExtension.launchOnIO
import com.bonepeople.android.widget.util.AppLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class FlowViewModel : ViewModel() {
    val executeTime = MutableStateFlow(2000L)
    val value1 = MutableStateFlow(0)
    private val emitTime = 550L

    fun emit1() {
        viewModelScope.launchOnIO {
            listOf(1, 2, 3, 3, 3, 4, 5, 5, 6, 7, 8, 9).forEach {
                AppLog.defaultLog.verbose("emit-$it")
                value1.value = it
                delay(emitTime)
            }
        }
    }
}