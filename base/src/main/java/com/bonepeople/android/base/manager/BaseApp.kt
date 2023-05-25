package com.bonepeople.android.base.manager

import android.app.Application
import androidx.startup.StartupHelper
import com.bonepeople.android.widget.util.AppStorage

abstract class BaseApp : Application() {
    protected abstract val appName: String

    override fun onCreate() {
        super.onCreate()
        StartupHelper.initializeAll(this)
        AppStorage.putString("com.bonepeople.android.key.APP_NAME", appName)
    }
}