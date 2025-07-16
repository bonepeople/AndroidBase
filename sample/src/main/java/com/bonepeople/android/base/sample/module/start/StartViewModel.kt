package com.bonepeople.android.base.sample.module.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonepeople.android.base.sample.global.UserManager
import com.bonepeople.android.base.util.CoroutineExtension.launchOnDefault
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class StartViewModel : ViewModel() {
    val pageState: MutableStateFlow<PageState> = MutableStateFlow(PageState.Init)
    private val initialized = AtomicBoolean(false)

    fun init() {
        if (!initialized.compareAndSet(false, true)) return
        viewModelScope.launchOnDefault {
            runCatching {
                pageState.value = PageState.Loading
                coroutineScope {
                    launch {
                        delay(1000)
                    }
                    launch {
                        UserManager.isLogin
                    }
                }
                pageState.value = PageState.Finish
            }.onFailure {
                pageState.value = PageState.Error
            }
        }
    }

    sealed interface PageState {
        object Init : PageState
        object Loading : PageState
        object Finish : PageState
        object Error : PageState
    }
}