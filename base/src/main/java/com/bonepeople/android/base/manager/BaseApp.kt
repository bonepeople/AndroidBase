package com.bonepeople.android.base.manager

import android.app.Application
import androidx.startup.StartupHelper
import com.bonepeople.android.widget.CoroutinesHolder
import com.bonepeople.android.widget.util.AppData
import kotlinx.coroutines.launch

abstract class BaseApp : Application() {
    protected abstract val appName: String

    override fun onCreate() {
        super.onCreate()
        CoroutinesHolder.io.launch {
            AppData.create("com.bonepeople.android.base.manager.BaseApp").putString("appName", appName)
        }
        StartupHelper.initializeAll(this)
    }
}