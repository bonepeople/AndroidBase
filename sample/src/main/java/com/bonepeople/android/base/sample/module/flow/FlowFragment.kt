package com.bonepeople.android.base.sample.module.flow

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.bonepeople.android.base.sample.databinding.FragmentFlowBinding
import com.bonepeople.android.base.util.FlowExtension.observeWithLifecycle
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.delay

class FlowFragment : ViewBindingFragment<FragmentFlowBinding>() {
    private val viewModel: FlowViewModel by viewModels()

    override fun initView() {
        views.titleView.title = "Flow Test"
        viewModel.value1.observeWithLifecycle(viewLifecycleOwner, Lifecycle.State.RESUMED) {
            AppLog.defaultLog.info("execute-$it")
            delay(viewModel.executeTime.value)
            AppLog.defaultLog.info("done-$it")
        }
        views.buttonEmit1.singleClick { viewModel.emit1() }
    }
}