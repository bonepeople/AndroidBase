package com.bonepeople.android.base.exception

import android.content.Intent
import android.os.Build
import androidx.core.content.FileProvider
import androidx.shade.EarthTime
import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppTime
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.LinkedList

/**
 * Default crash handling steps (logging, persisting report, opening/sharing).
 * Call these from a custom [CrashHandler.setCrashAction] or elsewhere when you handle crashes manually.
 */
@Suppress("Unused")
object CrashDefaultActions {
    /**
     * Default flow by environment:
     * - debug: log to console, write JSON report, then try to open it with an external viewer
     * - release: write JSON report only
     */
    suspend fun defaultCrashAction(thread: Thread, exception: Throwable) {
        withContext(Dispatchers.IO) {
            val file = saveCrashReportToFile(thread, exception)
            if (ApplicationHolder.debug) {
                printCrashLog(thread, exception)
                openCrashLogWithExternalViewer(file)
            }
        }
    }

    /**
     * Writes [exception] (and environment) to the app log.
     */
    fun printCrashLog(thread: Thread, exception: Throwable, logMessage: String? = null) {
        val message = logMessage ?: "uncaughtException @ ${thread.name}"
        AppLog.defaultLog.error(message, exception)
    }

    /**
     * Builds structured crash metadata; [thread] is treated as the crashed thread.
     */
    fun makeExceptionInfo(thread: Thread, exception: Throwable, withStack: Boolean = true): ExceptionInfo {
        return ExceptionInfo().apply {
            timestamp = EarthTime.now()
            time = AppTime.getDateTimeString(timestamp)
            this.thread = thread.name
            appVersion = "${ApplicationHolder.getVersionCode()}(${ApplicationHolder.getVersionName()})"
            osVersion = "${Build.VERSION.SDK_INT}(Android_${Build.VERSION.RELEASE})"
            manufacturer = Build.MANUFACTURER
            model = Build.MODEL
            systemAbi = AppGson.toJson(Build.SUPPORTED_ABIS)
            kotlin.runCatching {
                appAbi = System.getProperty("os.arch") ?: "unknown"
            }
            message = exception.message ?: ""
            if (withStack) {
                stack.add(exception.toString())
                saveStackTrace(exception, stack)
            }
        }
    }

    /**
     * Saves a pretty-printed JSON crash report.
     *
     * By default, the report is written under the current application cache directory:
     * /cache/crashReport/error_{timestamp}.txt.
     *
     * If [targetFile] is provided, the report will be written to the specified file instead.
     *
     * @param thread the thread where the crash occurred
     * @param exception the thrown exception
     * @param targetFile optional custom file location; if null, a default file will be created
     * @return the written file
     *
     * @throws IOException if an error occurs while writing the file
     */
    suspend fun saveCrashReportToFile(thread: Thread, exception: Throwable, targetFile: File? = null): File {
        return withContext(Dispatchers.IO) {
            val exceptionInfo = makeExceptionInfo(thread, exception)
            val webContent = GsonBuilder().setPrettyPrinting().create().toJson(exceptionInfo)
            val file = targetFile ?: run {
                val path = File(ApplicationHolder.app.cacheDir, "crashReport")
                path.mkdirs()
                File(path, "error_${exceptionInfo.timestamp}.txt")
            }
            FileOutputStream(file).use {
                it.write(webContent.toByteArray())
            }
            file
        }
    }

    /**
     * Tries to open the report with [Intent.ACTION_VIEW] (same behavior as the historical default).
     */
    fun openCrashLogWithExternalViewer(file: File) {
        val app = ApplicationHolder.app
        val authority = app.packageName + ".crash.provider"
        val uri = FileProvider.getUriForFile(app, authority, file)
        Intent(Intent.ACTION_VIEW).let {
            it.data = uri
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            kotlin.runCatching {
                app.startActivity(it)
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
}
