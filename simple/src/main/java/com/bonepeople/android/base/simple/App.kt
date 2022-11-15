package com.bonepeople.android.base.simple

import com.bonepeople.android.base.manager.BaseApp
import com.bonepeople.android.widget.util.AppLog

class App : BaseApp() {
    override val appName = "AndroidBaseSimple"

    override fun onCreate() {
        super.onCreate()
        AppLog.enable = true
    }
}