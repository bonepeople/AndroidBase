package com.bonepeople.android.base.simple.ui

import android.os.Bundle
import com.bonepeople.android.base.ViewBindingActivity
import com.bonepeople.android.base.activity.StandardActivity
import com.bonepeople.android.base.simple.databinding.ActivityMainBinding
import com.bonepeople.android.widget.util.AppView.singleClick

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun initView() {
        views.titleView.textViewTitleName.text = "BaseTestActivity"
        views.buttonUserManager.singleClick { StandardActivity.open(UserInfoFragment()) }
        views.buttonProductList.singleClick { StandardActivity.open(ProductListFragment()) }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}