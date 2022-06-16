package com.bonepeople.android.base.simple

import android.app.Application
import com.bonepeople.android.base.exception.ExceptionHandler
import com.bonepeople.android.widget.util.AppLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppLog.enable = true
        AppLog.tag = "AndroidBaseTag"
        ExceptionHandler.setCrashAction { _, exception ->
            AppLog.error("CrashException", exception)
        }
    }
}