package com.bonepeople.android.base.view

import com.bonepeople.android.base.viewbinding.ViewBindingDialogFragment
import com.bonepeople.android.base.databinding.DialogCustomLoadingBinding

class CustomLoadingDialog : ViewBindingDialogFragment<DialogCustomLoadingBinding>() {

    override fun initView() {
        views.root
        isCancelable = false
    }
}