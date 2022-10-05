package com.bonepeople.android.base.simple.ui

import android.os.Bundle
import com.bonepeople.android.base.ViewBindingFragment
import com.bonepeople.android.base.simple.databinding.FragmentTestBinding

class TestFragment : ViewBindingFragment<FragmentTestBinding>() {
    override fun initView() {
        views.root
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}