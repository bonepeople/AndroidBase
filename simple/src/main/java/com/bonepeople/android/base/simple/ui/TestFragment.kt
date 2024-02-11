package com.bonepeople.android.base.simple.ui

import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.simple.databinding.FragmentTestBinding
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppTime
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class TestFragment : ViewBindingFragment<FragmentTestBinding>() {
    override fun initView() {
        views.titleView.title = "测试"
        views.buttonTest.singleClick { startTest() }
    }

    private fun startTest() {
        launch {
            loadingDialog.show()
            measureTimeMillis {
                kotlin.runCatching {
                    test()
                }.getOrElse {
                    AppLog.defaultLog.error("测试异常：", it)
                }
            }.let {
                AppLog.defaultLog.debug("测试耗时：${AppTime.getTimeString(it)}s")
            }
            loadingDialog.dismiss()
        }
    }

    private suspend fun test() {
        delay(2000)
    }
}