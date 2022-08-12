package com.bonepeople.android.base.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bonepeople.android.base.databinding.DialogCustomLoadingBinding

class CustomLoadingDialog(private val manager: FragmentManager) : DialogFragment() {
    private val views by lazy { DialogCustomLoadingBinding.inflate(layoutInflater) }
    private var showing = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = views.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        dialog?.window?.run { setBackgroundDrawable(GradientDrawable()) }
    }

    override fun onDestroyView() {
        (views.root.parent as ViewGroup).removeView(views.root)
        showing = false
        super.onDestroyView()
    }

    fun show() {
        if (showing) return
        show(manager, null)
        showing = true
    }
}