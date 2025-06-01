package com.bonepeople.android.base.manager

import androidx.shade.EarthTime
import androidx.shade.Protector

@Suppress("Unused")
object CTime {
    fun current(): Long = Protector.protect { EarthTime.now() }
}