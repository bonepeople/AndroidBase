package com.bonepeople.android.base.view

import androidx.fragment.app.FragmentManager
import com.bonepeople.android.base.ViewBindingDialogFragment
import com.bonepeople.android.base.databinding.DialogCustomLoadingBinding

class CustomLoadingDialog(manager: FragmentManager) : ViewBindingDialogFragment<DialogCustomLoadingBinding>(manager) {

    override fun initView() {
        views.root
        isCancelable = false
    }
}