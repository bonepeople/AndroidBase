package com.bonepeople.android.base.manager

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppStorage
import java.lang.reflect.ParameterizedType

abstract class BaseUserManager<D> {
    val userId: String
        get() = AppStorage.getString(USER_ID)
    val userInfo: D
        get() {
            return kotlin.runCatching {
                val superclass = javaClass.genericSuperclass as ParameterizedType
                val subclass = superclass.actualTypeArguments[0] as Class<*>
                AppGson.gson.fromJson<D>(AppStorage.getString(USER_INFO), subclass)
            }.getOrNull() ?: defaultUserInfo
        }
    val isLogin: Boolean
        get() {
            return userId.isNotBlank()
        }

    fun login(info: D) {
        val login = !isLogin
        AppStorage.putString(USER_INFO, AppGson.toJson(info))
        AppStorage.putString(USER_ID, resolveUserId(info))
        if (login) {
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_LOGIN)
        } else {
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_UPDATE)
        }
    }

    fun logout() {
        if (isLogin) {
            AppStorage.putString(USER_INFO, "")
            AppStorage.putString(USER_ID, resolveUserId(defaultUserInfo))
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