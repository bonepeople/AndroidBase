package com.bonepeople.android.base.manager

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppStorage
import com.google.gson.reflect.TypeToken

abstract class BaseUserManager<D> {
    val userId: String
        get() = AppStorage.getString(USER_ID)
    val userInfo: D
        get() {
            return kotlin.runCatching {
                AppGson.gson.fromJson<D>(AppStorage.getString(USER_INFO), object : TypeToken<D>() {}.type)
            }.getOrNull() ?: defaultUserInfo
        }
    val isLogin: Boolean
        get() {
            return userId.isNotBlank()
        }

    fun login(userInfo: D) {
        if (!isLogin) {
            AppStorage.putString(USER_ID, resolveUserId(userInfo))
            AppStorage.putString(USER_INFO, AppGson.toJson(userInfo))
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_LOGIN)
        }
    }

    fun logout() {
        if (isLogin) {
            AppStorage.putString(USER_ID, resolveUserId(defaultUserInfo))
            AppStorage.putString(USER_INFO, "")
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_LOGOUT)
        }
    }

    abstract val defaultUserInfo: D
    abstract fun resolveUserId(userInfo: D): String

    companion object {
        private const val USER_ID = "com.bonepeople.android.key.USER_ID"
        private const val USER_INFO = "com.bonepeople.android.key.USER_INFO"
    }
}