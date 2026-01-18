package com.bonepeople.android.base.exception

import com.bonepeople.android.widget.ApplicationHolder
import com.bonepeople.android.widget.util.AppGson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Provides access to crash reports persisted by the default crash handler.
 */
@Suppress("Unused")
object CrashReportStore {
    /**
     * Loads all valid persisted crash reports, ordered from newest to oldest.
     *
     * A report that cannot be read or parsed is skipped without preventing other reports from being returned.
     */
    suspend fun loadCrashReports(): List<RecordInfo> {
        return withContext(Dispatchers.IO) {
            crashReportDirectory.listFiles()
                ?.asSequence()
                ?.filter { it.isFile }
                ?.mapNotNull { loadCrashReport(it) }
                ?.sortedByDescending { it.exceptionInfo.timestamp }
                ?.toList()
                .orEmpty()
        }
    }

    internal fun createCrashReportFile(timestamp: Long): File {
        crashReportDirectory.mkdirs()
        return File(crashReportDirectory, "error_$timestamp.txt")
    }

    private val crashReportDirectory: File
        get() = File(ApplicationHolder.app.cacheDir, "crashReport")

    private fun loadCrashReport(file: File): RecordInfo? {
        return kotlin.runCatching {
            val content = file.readText()
            RecordInfo(exceptionInfo = AppGson.toObject<ExceptionInfo>(content), file = file)
        }.getOrNull()
    }

    /**
     * A crash report persisted by [CrashDefaultActions].
     *
     * [file] identifies the persisted report so callers can export it or delete it after a successful upload.
     */
    class RecordInfo internal constructor(val exceptionInfo: ExceptionInfo, val file: File)
}