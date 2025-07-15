package com.bonepeople.android.base.sample.module.start

import com.bonepeople.android.base.sample.R
import com.bonepeople.android.base.sample.databinding.ActivityStartBinding
import com.bonepeople.android.base.viewbinding.ViewBindingActivity2
import com.bonepeople.android.widget.ApplicationHolder
import com.gyf.immersionbar.ktx.immersionBar

class StartActivity : ViewBindingActivity2<ActivityStartBinding>() {
    override fun setStatusBar() {
        immersionBar {
            transparentBar()
            statusBarDarkFont(false)
        }
    }

    override fun initView() {
        views.textVersion.text = getString(R.string.start_version_format, ApplicationHolder.getVersionName())
    }
}