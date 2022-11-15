package com.bonepeople.android.base.manager

import android.app.Application
import com.bonepeople.android.widget.util.AppStorage

abstract class BaseApp : Application() {
    protected abstract val appName: String

    override fun onCreate() {
        super.onCreate()
        AppStorage.putString("com.bonepeople.android.key.APP_NAME", appName)
    }
}