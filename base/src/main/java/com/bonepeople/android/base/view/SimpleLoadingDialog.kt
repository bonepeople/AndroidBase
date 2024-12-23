package com.bonepeople.android.base.view

import com.bonepeople.android.base.databinding.DialogSimpleLoadingBinding
import com.bonepeople.android.base.viewbinding.ViewBindingDialogFragment2

class SimpleLoadingDialog : ViewBindingDialogFragment2<DialogSimpleLoadingBinding>() {
    override fun initView() {
        views.root
        isCancelable = false
    }

    fun switchShow(showing: Boolean) {
        if (showing) show() else dismiss()
    }
}