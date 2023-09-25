package com.bonepeople.android.base.exception

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.FileProvider
import androidx.startup.Initializer
import com.bonepeople.android.shade.EarthTime
import com.bonepeople.android.shade.Lighting
import com.bonepeople.android.shade.Protector
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

/**
 * 崩溃处理类
 * + 可用于java全局未捕获异常的捕获处理，也可以用于对异常信息的封装
 * + 可调用[setCrashAction]方法设置捕获后的操作
 */
@Suppress("UNUSED")
object CrashHandler : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var crashAction: suspend (message: String, exception: Throwable) -> Unit = CrashHandler::defaultCrashAction

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        runBlocking {
            kotlin.runCatching {
                coroutineScope {
                    launch {
                        crashAction("uncaughtException @ ${thread.name}", exception)
                    }
                    launch {
                        val exceptionInfo = makeExceptionInfo(exception)
                        val json = AppGson.toJson(exceptionInfo)
                        Lighting.c5("shade.exception", 1, "崩溃异常", json)
                    }
                }
            }
        }
        defaultHandler?.uncaughtException(thread, exception)
//        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 设置捕获全局异常后的处理逻辑
     */
    fun setCrashAction(action: suspend (message: String, exception: Throwable) -> Unit) {
        crashAction = action
    }

    /**
     * 默认处理方式
     */
    suspend fun defaultCrashAction(message: String, exception: Throwable) {
        if (!ApplicationHolder.debug) return
        withContext(Dispatchers.IO) {
            //输出错误日志到控制台
            AppLog.error(message, exception)
            //收集错误信息并生成日志内容
            val exceptionInfo = makeExceptionInfo(exception)
            val webContent = GsonBuilder().setPrettyPrinting().create().toJson(exceptionInfo)
            //将错误日志写到缓存目录
            val path = File(ApplicationHolder.app.cacheDir, "crashReport")
            path.mkdirs()
            val file = File(path, "error_${exceptionInfo.timestamp}.txt")
            FileOutputStream(file).use {
                it.write(webContent.toByteArray())
            }
            //通过浏览器展示日志内容
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

    /**
     * 获取异常的数据信息
     * + 会获取当前线程的名称存储为异常线程
     * @param exception 需要处理的异常
     * @param withStack 是否包括调用栈信息，默认包含
     * @return 提取异常信息及当前环境信息并封装的数据类，后续可以在[ExceptionInfo.extra]中放入额外数据
     */
    fun makeExceptionInfo(exception: Throwable, withStack: Boolean = true): ExceptionInfo {
        return ExceptionInfo().apply {
            timestamp = EarthTime.now()
            time = AppTime.getDateTimeString(timestamp)
            this.thread = Thread.currentThread().name
            appVersion = "${ApplicationHolder.getVersionCode()}(${ApplicationHolder.getVersionName()})"
            osVersion = "${Build.VERSION.SDK_INT}(Android_${Build.VERSION.RELEASE})"
            manufacturer = Build.MANUFACTURER
            model = Build.MODEL
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

    /**
     * ExceptionHandler的初始化逻辑
     */
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