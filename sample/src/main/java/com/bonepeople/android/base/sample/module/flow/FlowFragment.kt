package com.bonepeople.android.base.sample.module.flow

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bonepeople.android.base.sample.databinding.FragmentFlowBinding
import com.bonepeople.android.base.util.FlowExtension.newObserveWithLifecycle
import com.bonepeople.android.base.util.FlowExtension.observeLatestWithLifecycle
import com.bonepeople.android.base.util.FlowExtension.observeWithLifecycle
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FlowFragment : ViewBindingFragment<FragmentFlowBinding>() {
    private val viewModel: FlowViewModel by viewModels()

    override fun initView() {
        views.titleView.title = "Flow Test"
        // 1
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.value1.collect {
//                AppLog.defaultLog.info("1-$it")
//                delay(viewModel.executeTime.value)
//                AppLog.defaultLog.info("1.done-$it")
//            }
//        }

        // 1 前台状态时收到的值会依次执行，无论是否切换至后台
        viewModel.value1.newObserveWithLifecycle(viewLifecycleOwner, Lifecycle.State.RESUMED) {
            AppLog.defaultLog.info("1-$it")
            delay(viewModel.executeTime.value)
            AppLog.defaultLog.info("1.done-$it")
        }

        // 2 前台状态时收到的值会依次执行，无论是否切换至后台
        viewModel.value1.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED).distinctUntilChanged().onEach {
            AppLog.defaultLog.debug("2-$it")
            delay(viewModel.executeTime.value)
            AppLog.defaultLog.debug("2.done-$it")
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // 3
//        viewModel.value1.observeLatestWithLifecycle(viewLifecycleOwner, Lifecycle.State.RESUMED) {
//            AppLog.defaultLog.warn("3-$it")
//            delay(viewModel.executeTime.value)
//            AppLog.defaultLog.warn("3.done-$it")
//        }

        views.buttonEmit1.singleClick { viewModel.emit1() }
    }

    override fun onResume() {
        super.onResume()
        AppLog.defaultLog.verbose("onPause")
    }

    override fun onPause() {
        AppLog.defaultLog.verbose("onPause")
        super.onPause()
    }

    fun <T> Flow<T>.collectConflatedInLifecycle(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        scope: CoroutineScope = lifecycleOwner.lifecycleScope,
        block: suspend (T) -> Unit
    ) {
        scope.launch {
            val conflatedChannel = Channel<T>(capacity = Channel.CONFLATED)

            // Step 1: 监听 lifecycle，并将值发送到 conflatedChannel
            launch {
                this@collectConflatedInLifecycle
                    .flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
                    .collect {
                        conflatedChannel.trySend(it) // 只保留最新值
                    }
            }

            // Step 2: 顺序消费 conflatedChannel 的值，每次等 block 执行完成再处理下一个
            launch {
                for (value in conflatedChannel) {
                    block(value)
                }
            }
        }
    }

    private fun <T> StateFlow<T>.newObserve(lifecycleOwner: LifecycleOwner, minActiveState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Flow<T> {
        return callbackFlow {
            lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
                this@newObserve.collect {
                    AppLog.defaultLog.info("")
                    send(it)
                }
            }
            close()
        }
    }
}