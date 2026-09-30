package com.bonepeople.android.base.util

import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.bonepeople.android.widget.ActivityHolder
import com.bonepeople.android.widget.util.permission.AppPermission
import com.bonepeople.android.widget.util.permission.PermissionResult
import com.bonepeople.android.widget.util.permission.PermissionStatus
import kotlinx.coroutines.launch

object PermissionHelper {
    fun requestSingle(permission: String, onResult: (PermissionStatus) -> Unit) {
        requestMultiple(listOf(permission)) { result ->
            onResult(result.permissionStatuses.getValue(permission))
        }
    }

    fun requestMultiple(permissions: Collection<String>, onResult: (PermissionResult) -> Unit) {
        val activity = ActivityHolder.getTopActivity() as? ComponentActivity ?: throw IllegalStateException("No active ComponentActivity is available to request permissions.")
        activity.lifecycleScope.launch {
            onResult(AppPermission.request(*permissions.toTypedArray()))
        }
    }
}