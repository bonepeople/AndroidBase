package com.bonepeople.android.base.simple

import android.os.Bundle
import android.view.View
import com.bonepeople.android.base.ViewBindingActivity
import com.bonepeople.android.base.simple.databinding.ActivityMainBinding
import com.bonepeople.android.widget.util.singleClick

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun initView() {
        views.titleView.imageViewTitleBack.run {
            singleClick { onBackPressed() }
            visibility = View.VISIBLE
        }
        views.titleView.textViewTitleName.text = "SimpleActivity"
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}