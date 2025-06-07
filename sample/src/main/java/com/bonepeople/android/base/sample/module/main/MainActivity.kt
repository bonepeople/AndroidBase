package com.bonepeople.android.base.sample.module.main

import com.bonepeople.android.base.viewbinding.ViewBindingActivity
import com.bonepeople.android.base.activity.StandardActivity
import com.bonepeople.android.base.sample.databinding.ActivityMainBinding
import com.bonepeople.android.base.sample.module.recycleradapter.ProductListFragment
import com.bonepeople.android.base.sample.module.test.TestFragment
import com.bonepeople.android.base.sample.module.usermanager.UserInfoFragment
import com.bonepeople.android.widget.util.AppView.singleClick

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun initView() {
        views.titleView.title = "BaseTestActivity"
        views.buttonUserManager.singleClick { StandardActivity.open(UserInfoFragment()) }
        views.buttonProductList.singleClick { StandardActivity.open(ProductListFragment()) }
        views.buttonTest.singleClick { StandardActivity.open(TestFragment()) }
    }
}