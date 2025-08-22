package com.bonepeople.android.base.sample.module.start

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bonepeople.android.base.sample.R
import com.bonepeople.android.base.sample.databinding.ActivityStartBinding
import com.bonepeople.android.base.sample.module.home.HomeActivity
import com.bonepeople.android.base.util.FlowExtension.observeWithLifecycle
import com.bonepeople.android.base.viewbinding.ViewBindingActivity2
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppToast
import com.gyf.immersionbar.ktx.immersionBar

class StartActivity : ViewBindingActivity2<ActivityStartBinding>() {
    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setOnExitAnimationListener { splashScreenView -> splashScreenView.remove() }
        }
        super.onCreate(savedInstanceState)
    }

    override fun setStatusBar() {
        immersionBar {
            transparentBar()
            statusBarDarkFont(false)
            navigationBarDarkIcon(false)
        }
    }

    override fun initView() {
        views.textVersion.text = getString(R.string.start_version_format, ApplicationHolder.getVersionName())
        viewModel.pageState.observeWithLifecycle(this) { pageState ->
            when (pageState) {
                StartViewModel.PageState.Init, StartViewModel.PageState.Loading -> Unit
                StartViewModel.PageState.Finish -> navigateToHome()
                StartViewModel.PageState.Error -> AppToast.show(getString(R.string.start_init_error))
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel.init()
    }

    private fun navigateToHome() {
        HomeActivity.open()
        finishAfterTransition()
    }
}