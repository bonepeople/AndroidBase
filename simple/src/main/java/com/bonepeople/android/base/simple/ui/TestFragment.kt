package com.bonepeople.android.base.simple.ui

import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.simple.databinding.FragmentTestBinding

class TestFragment : ViewBindingFragment<FragmentTestBinding>() {
    override fun initView() {
        views.root
    }
}