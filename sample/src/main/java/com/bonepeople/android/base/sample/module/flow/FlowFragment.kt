package com.bonepeople.android.base.sample.module.flow

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bonepeople.android.base.sample.databinding.FragmentFlowBinding
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FlowFragment : ViewBindingFragment<FragmentFlowBinding>() {
    private val viewModel: FlowViewModel by viewModels()

    override fun initView() {
        views.titleView.title = "Flow Test"
        // 1 Traditional method
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.value1.collect {
                AppLog.defaultLog.info("start-$it")
                delay(viewModel.executeTime.value)
                AppLog.defaultLog.info("done-$it")
            }
        }
        // 2 Values received while in the foreground state will be executed one by one
        viewModel.value1.observeWithLifecycle1(viewLifecycleOwner, Lifecycle.State.RESUMED) {
            AppLog.defaultLog.debug("                 start-$it")
            delay(viewModel.executeTime.value)
            AppLog.defaultLog.debug("                 done-$it")
        }
        // 3 Only the latest value received in the foreground state will be executed
        viewModel.value1.observeWithLifecycle2(viewLifecycleOwner, Lifecycle.State.RESUMED) {
            AppLog.defaultLog.warn("                                  start-$it")
            delay(viewModel.executeTime.value)
            AppLog.defaultLog.warn("                                  done-$it")
        }

        views.buttonEmit1.singleClick { viewModel.emit1() }
    }

    override fun onResume() {
        super.onResume()
        AppLog.defaultLog.verbose("onResume")
    }

    override fun onPause() {
        AppLog.defaultLog.verbose("onPause")
        super.onPause()
    }

    private fun <T> StateFlow<T>.observeWithLifecycle1(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).distinctUntilChanged().onEach(action).launchIn(owner.lifecycleScope)
    }

    private fun <T> StateFlow<T>.observeWithLifecycle2(owner: LifecycleOwner, activeState: Lifecycle.State = Lifecycle.State.STARTED, action: suspend (T) -> Unit): Job {
        return flowWithLifecycle(owner.lifecycle, activeState).distinctUntilChanged().conflate().onEach(action).launchIn(owner.lifecycleScope)
    }
}