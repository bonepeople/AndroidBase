package com.bonepeople.android.base.sample.module.start

import androidx.lifecycle.ViewModel
import com.bonepeople.android.base.sample.global.UserManager
import com.bonepeople.android.base.util.ViewModelExtension.initializeOnce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {
    val pageState: MutableStateFlow<PageState> = MutableStateFlow(PageState.Init)

    fun init() = initializeOnce {
        runCatching {
            pageState.value = PageState.Loading
            coroutineScope {
                launch {
                    delay(2000)
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

    sealed interface PageState {
        object Init : PageState
        object Loading : PageState
        object Finish : PageState
        object Error : PageState
    }
}