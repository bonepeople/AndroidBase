package com.bonepeople.android.base.sample

import com.bonepeople.android.base.foundation.QuickApplication
import com.bonepeople.android.widget.util.AppLog

class App : QuickApplication() {
    override val appName = "AndroidBaseSample"

    override fun onCreate() {
        super.onCreate()
        AppLog.enable = true
    }
}