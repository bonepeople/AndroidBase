package com.bonepeople.android.base.foundation

import androidx.shade.migrate.DataMigrateInfo
import androidx.shade.migrate.DataMigrateUtil

abstract class QuickDataMigrateManager {
    abstract val dataId: String
    abstract val migrateList: List<Pair<IntRange, suspend () -> Unit>>

    suspend fun startMigrate() {
        DataMigrateUtil.migrate(
            dataId = "base_$dataId",
            migrateList = migrateList.map {
                object : DataMigrateInfo {
                    override val range: IntRange = it.first
                    override val action: suspend () -> Unit = it.second
                }
            }
        )
    }
}