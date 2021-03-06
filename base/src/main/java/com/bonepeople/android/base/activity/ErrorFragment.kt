package com.bonepeople.android.base.activity

import android.os.Bundle
import android.view.View
import com.bonepeople.android.base.ViewBindingFragment
import com.bonepeople.android.base.databinding.FragmentErrorBinding
import com.bonepeople.android.widget.util.singleClick

class ErrorFragment : ViewBindingFragment<FragmentErrorBinding>() {
    override fun initView() {
        views.titleView.imageViewTitleBack.run {
            visibility = View.VISIBLE
            singleClick { requireActivity().onBackPressed() }
        }
        views.titleView.textViewTitleName.text = "Error"
    }

    override fun initData(savedInstanceState: Bundle?) {
        val content = "no fragment to show"
        views.textViewError.text = content
    }
}