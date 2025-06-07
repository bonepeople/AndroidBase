package com.bonepeople.android.base.sample.module.test

import androidx.lifecycle.lifecycleScope
import com.bonepeople.android.base.sample.databinding.FragmentTestBinding
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppTime
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class TestFragment : ViewBindingFragment<FragmentTestBinding>() {
    override fun initView() {
        views.titleView.title = "Test"
        views.buttonTest.singleClick { startTest() }
    }

    private fun startTest() {
        viewLifecycleOwner.lifecycleScope.launch {
            loadingDialog.show()
            measureTimeMillis {
                kotlin.runCatching {
                    test()
                }.getOrElse {
                    AppLog.defaultLog.error("Test error：", it)
                }
            }.let {
                AppLog.defaultLog.debug("Test time：${AppTime.getTimeString(it)}s")
            }
            loadingDialog.dismiss()
        }
    }

    private suspend fun test() {
        delay(2000)
    }
}