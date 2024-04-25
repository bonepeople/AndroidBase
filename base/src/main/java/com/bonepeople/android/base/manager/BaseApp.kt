package com.bonepeople.android.base.manager

import android.app.Application
import androidx.startup.StartupHelper

abstract class BaseApp : Application() {
    protected abstract val appName: String

    override fun onCreate() {
        super.onCreate()
        StartupHelper.initializeAll(this)
    }
}