package com.bonepeople.android.base.simple.ui

import com.bonepeople.android.base.viewbinding.ViewBindingActivity
import com.bonepeople.android.base.activity.StandardActivity
import com.bonepeople.android.base.simple.databinding.ActivityMainBinding
import com.bonepeople.android.widget.util.AppView.singleClick

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun initView() {
        views.titleView.title = "BaseTestActivity"
        views.buttonUserManager.singleClick { StandardActivity.open(UserInfoFragment()) }
        views.buttonProductList.singleClick { StandardActivity.open(ProductListFragment()) }
        views.buttonTest.singleClick { StandardActivity.open(TestFragment()) }
    }
}