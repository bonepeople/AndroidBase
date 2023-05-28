package com.bonepeople.android.base.activity

import android.os.Bundle
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.databinding.FragmentErrorBinding

class ErrorFragment : ViewBindingFragment<FragmentErrorBinding>() {
    override fun initView() {
        views.titleView.onBackClick { onBackPressed() }
        views.titleView.title = "Error"
    }

    override fun initData(savedInstanceState: Bundle?) {
        val content = "no fragment to show"
        views.textViewError.text = content
    }
}