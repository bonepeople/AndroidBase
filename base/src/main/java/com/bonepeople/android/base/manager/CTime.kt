package com.bonepeople.android.base.manager

import com.bonepeople.android.shade.EarthTime
import com.bonepeople.android.shade.Protector

@Suppress("UNUSED")
object CTime {
    fun current(): Long = Protector.protect { EarthTime.now() }
}