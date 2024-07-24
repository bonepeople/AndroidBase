package com.bonepeople.android.base.exception

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.FileProvider
import androidx.startup.Initializer
import com.bonepeople.android.shade.EarthTime
import com.bonepeople.android.shade.Lighting
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppTime
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.coroutines.coroutineContext

@Suppress("UNUSED")
object CrashHandler : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var crashAction: suspend (message: String, exception: Throwable) -> Unit = CrashHandler::defaultCrashAction
    var runCrashAction = ApplicationHolder.debug

    override fun uncaughtException(thread: Thread, exception: Throwable) {
//        runBlocking {
//            kotlin.runCatching {
//                coroutineScope {
//                    if ((exception.message ?: "").startsWith("[${ApplicationHolder.getPackageName()}]")) {
//                        return@coroutineScope
//                    }
//                    launch {
//                        if (runCrashAction) {
//                            crashAction("uncaughtException @ ${thread.name}", exception)
//                        }
//                    }
//                    launch {
//                        val exceptionInfo = makeExceptionInfo(exception)
//                        val json = AppGson.toJson(exceptionInfo)
//                        Lighting.c5("shade.exception", 1, "crash", json)
//                    }
//                }
//            }
//        }
        defaultHandler?.uncaughtException(thread, exception)
//        android.os.Process.killProcess(android.os.Process.myPid())
    }

    fun setCrashAction(action: suspend (message: String, exception: Throwable) -> Unit) {
        crashAction = action
    }

    suspend fun defaultCrashAction(message: String, exception: Throwable) {
        val context = coroutineContext
        withContext(Dispatchers.IO) {
            AppLog.defaultLog.error(message, exception)
            val exceptionInfo = withContext(context) {
                makeExceptionInfo(exception)
            }
            val webContent = GsonBuilder().setPrettyPrinting().create().toJson(exceptionInfo)
            val path = File(ApplicationHolder.app.cacheDir, "crashReport")
            path.mkdirs()
            val file = File(path, "error_${exceptionInfo.timestamp}.txt")
            FileOutputStream(file).use {
                it.write(webContent.toByteArray())
            }
            val authority = ApplicationHolder.app.packageName + ".crash.provider"
            val uri = FileProvider.getUriForFile(ApplicationHolder.app, authority, file)
            Intent(Intent.ACTION_VIEW).let {
                it.data = uri
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                kotlin.runCatching {
                    ApplicationHolder.app.startActivity(it)
                }
            }
        }
    }

    fun makeExceptionInfo(exception: Throwable, withStack: Boolean = true): ExceptionInfo {
        return ExceptionInfo().apply {
            timestamp = EarthTime.now()
            time = AppTime.getDateTimeString(timestamp)
            this.thread = Thread.currentThread().name
            appVersion = "${ApplicationHolder.getVersionCode()}(${ApplicationHolder.getVersionName()})"
            osVersion = "${Build.VERSION.SDK_INT}(Android_${Build.VERSION.RELEASE})"
            manufacturer = Build.MANUFACTURER
            model = Build.MODEL
            systemAbi = AppGson.toJson(Build.SUPPORTED_ABIS)
            kotlin.runCatching {
                appAbi = System.getProperty("os.arch") ?: "unknown"
            }
            this.message = exception.message ?: ""
            if (withStack) {
                stack.add(exception.toString())
                saveStackTrace(exception, stack)
            }
        }
    }

    private fun saveStackTrace(exception: Throwable, stackList: LinkedList<String>) {
        exception.stackTrace.forEach {
            stackList.add(it.toString())
        }
        exception.cause?.let {
            stackList.add("** Caused by: $it")
            saveStackTrace(it, stackList)
        }
    }

    class StartUp : Initializer<CrashHandler> {
        override fun create(context: Context): CrashHandler {
            defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler)
            return CrashHandler
        }

        override fun dependencies(): List<Class<out Initializer<*>>> {
            return listOf(ApplicationHolder.StartUp::class.java)
        }
    }
}