package com.bonepeople.android.base.sample.module.home

import android.app.Activity
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonepeople.android.base.activity.StandardActivity
import com.bonepeople.android.base.sample.databinding.ActivityHomeBinding
import com.bonepeople.android.base.sample.module.test.TestFragment
import com.bonepeople.android.base.sample.module.test.flow.FlowFragment
import com.bonepeople.android.base.sample.module.test.recycleradapter.ProductListFragment
import com.bonepeople.android.base.sample.module.test.usermanager.UserInfoFragment
import com.bonepeople.android.base.viewbinding.ViewBindingActivity2
import com.bonepeople.android.widget.ActivityHolder
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.view.LinearItemDecoration
import com.gyf.immersionbar.ktx.immersionBar

class HomeActivity : ViewBindingActivity2<ActivityHomeBinding>() {
    private val modules: List<HomeModuleInfo> = listOf(
        HomeModuleInfo("User Manager", "UserManager usage example") { StandardActivity.open(UserInfoFragment()) },
        HomeModuleInfo("List Adapter", "ViewBindingRecyclerAdapter demo") { StandardActivity.open(ProductListFragment()) },
        HomeModuleInfo("Flow Extension", "FlowExtension utilities demo") { StandardActivity.open(FlowFragment()) },
        HomeModuleInfo("General Test", "Basic components and utilities test entry") { StandardActivity.open(TestFragment()) },
    )

    override fun setStatusBar() {
        immersionBar {
            transparentBar()
        }
    }

    override fun initView() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAndRemoveTask()
            }
        })
        views.recyclerView.layoutManager = LinearLayoutManager(this)
        views.recyclerView.addItemDecoration(LinearItemDecoration(12f))
        views.recyclerView.adapter = HomeModuleAdapter(modules)
        views.textVersion.text = "Version ${ApplicationHolder.getVersionName()}"
    }

    companion object {
        fun open() {
            ActivityHolder.getTopActivity()?.let { activity: Activity ->
                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity.startActivity(intent)
            }
        }
    }
}