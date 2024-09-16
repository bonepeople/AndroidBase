package com.bonepeople.android.base.manager

import androidx.shade.Lighting
import com.bonepeople.android.widget.CoroutinesHolder
import kotlinx.coroutines.launch

@Suppress("Unused")
object Clog {
    fun u(type: String, code: Int, name: String, message: String) {
        CoroutinesHolder.default.launch {
            Lighting.c5(type, code, name, message)
        }
    }
}