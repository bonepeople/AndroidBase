package com.bonepeople.android.base.exception

import android.content.Context
import androidx.shade.Lighting
import androidx.startup.Initializer
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppGson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout

/**
 * Crash handling utility
 *
 * + Captures global uncaught Java exceptions and wraps exception info
 * + Use [setCrashActionWithThread] to define the behavior after a crash is caught and to customize the exception handling flow
 */
@Suppress("Unused")
object CrashHandler : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var crashAction: suspend (thread: Thread, exception: Throwable) -> Unit = CrashDefaultActions::defaultCrashAction
    @Deprecated(
        message = "Will be removed in 1.9.0. To control the exception handling flow, use setCrashActionWithThread instead."
    )
    var runCrashAction = true

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        if (CrashExceptionStore.shouldHandle(exception)) {
            runBlocking {
                supervisorScope {
                    if ((exception.message ?: "").startsWith("[${ApplicationHolder.getPackageName()}]")) {
                        return@supervisorScope
                    }
                    launch {
                        @Suppress("DEPRECATION")
                        if (runCrashAction) {
                            kotlin.runCatching {
                                crashAction(thread, exception)
                            }
                        }
                    }
                    launch {
                        kotlin.runCatching {
                            withTimeout(2_000L) {
                                val exceptionInfo = CrashDefaultActions.makeExceptionInfo(thread, exception)
                                val json = AppGson.toJson(exceptionInfo)
                                Lighting.c5("shade.exception", 1, "Crash Exception", json)
                            }
                        }
                    }
                }
            }
        }
        defaultHandler?.uncaughtException(thread, exception)
//        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * Sets the action to perform when a global exception is caught.
     * Receives the same [thread] and [exception] as [Thread.UncaughtExceptionHandler.uncaughtException].
     */
    fun setCrashActionWithThread(action: suspend (thread: Thread, exception: Throwable) -> Unit) {
        crashAction = action
    }

    @Deprecated(
        message = "Use setCrashActionWithThread(action) instead. This overload will be removed in 1.9.0.",
        replaceWith = ReplaceWith(
            expression = "setCrashActionWithThread { thread, exception -> action(\"uncaughtException @ \${thread.name}\", exception) }"
        )
    )
    fun setCrashAction(action: suspend (message: String, exception: Throwable) -> Unit) {
        crashAction = { thread, exception ->
            action("uncaughtException @ ${thread.name}", exception)
        }
    }

    @Deprecated(
        message = "Use CrashDefaultActions.defaultCrashAction(thread, exception). This overload will be removed in 1.9.0.",
        replaceWith = ReplaceWith(
            expression = "CrashDefaultActions.defaultCrashAction(Thread.currentThread(), exception)",
            imports = ["com.bonepeople.android.base.exception.CrashDefaultActions"]
        )
    )
    suspend fun defaultCrashAction(@Suppress("UNUSED_PARAMETER") message: String, exception: Throwable) {
        CrashDefaultActions.defaultCrashAction(Thread.currentThread(), exception)
    }

    @Deprecated(
        message = "Use CrashDefaultActions.makeExceptionInfo(thread, exception) for the crashed thread. This overload will be removed in 1.9.0.",
        replaceWith = ReplaceWith(
            expression = "CrashDefaultActions.makeExceptionInfo(Thread.currentThread(), exception, withStack)",
            imports = ["com.bonepeople.android.base.exception.CrashDefaultActions"]
        )
    )
    fun makeExceptionInfo(exception: Throwable, withStack: Boolean = true): ExceptionInfo {
        return CrashDefaultActions.makeExceptionInfo(Thread.currentThread(), exception, withStack)
    }

    /**
     * CrashHandler initializer for app startup
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