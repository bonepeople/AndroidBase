package com.bonepeople.android.base.sample

import com.bonepeople.android.base.manager.BaseApp
import com.bonepeople.android.widget.util.AppLog

class App : BaseApp() {
    override val appName = "AndroidBaseSample"

    override fun onCreate() {
        super.onCreate()
        AppLog.enable = true
    }
}