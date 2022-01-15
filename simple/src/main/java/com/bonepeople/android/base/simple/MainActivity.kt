package com.bonepeople.android.base.simple

import android.os.Bundle
import com.bonepeople.android.base.ViewBindingActivity
import com.bonepeople.android.base.simple.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun initView() {
        views.root
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}