package com.bonepeople.android.base.manager

import android.app.Application
import androidx.startup.StartupHelper
import com.bonepeople.android.widget.util.AppData
import com.bonepeople.android.widget.util.AppLog

abstract class BaseApp : Application() {
    private val build = "20240813-192738"
    protected abstract val appName: String

    override fun onCreate() {
        super.onCreate()
        StartupHelper.initializeAll(this)
        AppData.default.putStringSync("com.bonepeople.android.key.APP_NAME", appName)
        AppLog.defaultLog.verbose("build: $build")
    }
}