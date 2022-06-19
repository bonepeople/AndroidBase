package com.bonepeople.android.base.exception

import android.content.Context
import android.os.Build
import androidx.startup.Initializer
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * 崩溃处理类
 *
 * + 可用于java全局未捕获异常的捕获处理，也可以用于对异常信息的封装
 * + 可调用[setCrashAction]方法设置捕获后的操作
 */
object CrashHandler : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var crashAction: (message: String, exception: Throwable) -> Unit = CrashHandler::defaultCrashAction

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        kotlin.runCatching {
            crashAction.invoke("uncaughtException @ ${thread.name}", exception)
        }
        defaultHandler?.uncaughtException(thread, exception)
//        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 设置捕获全局异常后的处理逻辑
     */
    fun setCrashAction(action: (message: String, exception: Throwable) -> Unit) {
        crashAction = action
    }

    /**
     * 默认处理方式
     */
    fun defaultCrashAction(message: String, exception: Throwable) {
        runBlocking {
            coroutineScope {
                AppLog.error(message, exception)
            }
        }
    }

    /**
     * 获取异常的数据信息
     * @param exception 需要处理的异常
     * @param withStack 是否包括调用栈信息，默认包含
     * @return 提取异常信息及当前环境信息并封装的数据类，后续可以在[ExceptionInfo.extra]中放入额外数据
     */
    fun makeExceptionInfo(exception: Throwable, withStack: Boolean = true): ExceptionInfo {
        return ExceptionInfo().apply {
            timestamp = System.currentTimeMillis()
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
            stackList.add("Caused by: $it")
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