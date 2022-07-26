package com.bonepeople.android.base.simple

import android.os.Bundle
import com.bonepeople.android.base.ViewBindingActivity
import com.bonepeople.android.base.activity.StandardActivity
import com.bonepeople.android.base.simple.databinding.ActivityMainBinding
import com.bonepeople.android.base.simple.fragment.TestFragment
import com.bonepeople.android.widget.util.singleClick

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun initView() {
        views.titleView.textViewTitleName.text = "SimpleActivity"
        views.buttonOpenActivity.singleClick { openStandardActivity() }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    private fun openStandardActivity() {
        StandardActivity.open(TestFragment())
    }
}