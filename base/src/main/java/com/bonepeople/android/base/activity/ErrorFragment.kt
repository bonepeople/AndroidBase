package com.bonepeople.android.base.activity

import android.os.Bundle
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.databinding.FragmentErrorBinding
import com.bonepeople.android.widget.util.AppView.show
import com.bonepeople.android.widget.util.AppView.singleClick

class ErrorFragment : ViewBindingFragment<FragmentErrorBinding>() {
    override fun initView() {
        views.titleView.views.imageViewTitleBack.run {
            singleClick { onBackPressed() }
            show()
        }
        views.titleView.views.textViewTitleName.text = "Error"
    }

    override fun initData(savedInstanceState: Bundle?) {
        val content = "no fragment to show"
        views.textViewError.text = content
    }
}