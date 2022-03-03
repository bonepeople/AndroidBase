package com.bonepeople.android.base.exception

import android.os.Build
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppTime
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * 异常处理类
 *
 * 可用于java全局未捕获异常的捕获处理，也可以用于对异常信息的封装
 * + 需要调用[setCrashAction]方法设置捕获后的操作，处理完相关操作后会杀死当前进程
 */
object ExceptionHandler : Thread.UncaughtExceptionHandler {
    private var crashAction: suspend (exceptionInfo: ExceptionInfo, exception: Throwable) -> Unit = { _, _ -> }
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        AppLog.error("uncaughtException @ ${thread.name}", exception)
        runBlocking {
            crashAction.invoke(makeExceptionInfo(exception), exception)
        }
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 设置捕获全局异常后的处理逻辑
     *
     * 调用此方法会设置全局异常捕获的处理类为当前实例
     */
    fun setCrashAction(action: suspend (exceptionInfo: ExceptionInfo, exception: Throwable) -> Unit) {
        crashAction = action
        Thread.setDefaultUncaughtExceptionHandler(this)
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
                saveStackTrace(exception, stack)
            }
        }
    }

    private fun saveStackTrace(exception: Throwable, stackList: LinkedList<String>) {
        stackList.add(exception.toString())
        exception.stackTrace.forEach {
            stackList.add(it.toString())
        }
        exception.cause?.let {
            saveStackTrace(it, stackList)
        }
    }
}