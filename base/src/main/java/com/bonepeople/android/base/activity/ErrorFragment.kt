package com.bonepeople.android.base.activity

import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.databinding.FragmentErrorBinding

class ErrorFragment : ViewBindingFragment<FragmentErrorBinding>() {
    override fun initView() {
        views.titleView.onBackClick { onBackPressed() }
        views.titleView.title = "Error"
        views.textViewError.text = "no fragment to show"
    }
}