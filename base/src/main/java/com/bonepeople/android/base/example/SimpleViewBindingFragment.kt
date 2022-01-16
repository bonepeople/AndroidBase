package com.bonepeople.android.base.example

import android.os.Bundle
import com.bonepeople.android.base.ViewBindingFragment
import com.bonepeople.android.base.databinding.FragmentSimpleBinding

class SimpleViewBindingFragment(title: String) : ViewBindingFragment<FragmentSimpleBinding>() {

    init {
        arguments = Bundle().apply {
            putString("title", title)
        }
    }

    override fun initView() {
        views.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        val title = arguments?.getString("title", "")
    }
}