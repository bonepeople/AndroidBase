package com.bonepeople.android.base.example

import android.os.Bundle
import com.bonepeople.android.base.ViewBindingFragment
import com.bonepeople.android.base.databinding.FragmentSimpleBinding

class SimpleViewBindingFragment : ViewBindingFragment<FragmentSimpleBinding>() {

    override fun initView() {
        views.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        val title = arguments?.getString("title", "") ?: ""
    }

    companion object {
        fun getInstance(title: String): SimpleViewBindingFragment {
            return SimpleViewBindingFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                }
            }
        }
    }
}