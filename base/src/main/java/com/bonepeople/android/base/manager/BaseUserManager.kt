package com.bonepeople.android.base.manager

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppStorage
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseUserManager<D> {
    private val userClass: Class<*> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    private val defaultUserInfo: D = userClass.newInstance() as D
    var token: String = USER_TOKEN
        get() {
            if (field == USER_TOKEN) {
                field = AppStorage.getString(USER_TOKEN)
            }
            return field
        }
        set(value) {
            AppStorage.putString(USER_TOKEN, value)
            field = value
        }
    val userId: String
        get() = AppStorage.getString(USER_ID)
    val userInfo: D
        get() = AppGson.gson.fromJson<D>(AppStorage.getString(USER_INFO), userClass) ?: defaultUserInfo

    val isLogin: Boolean
        get() {
            return userId.isNotBlank()
        }

    fun login(newInfo: D) {
        val login = !isLogin
        AppStorage.putString(USER_INFO, AppGson.toJson(newInfo))
        AppStorage.putString(USER_ID, resolveUserId(newInfo))
        if (login) {
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_LOGIN)
        } else {
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_UPDATE)
        }
    }

    fun logout() {
        if (isLogin) {
            token = ""
            AppStorage.putString(USER_INFO, "")
            AppStorage.putString(USER_ID, resolveUserId(defaultUserInfo))
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_LOGOUT)
        }
    }

    abstract fun resolveUserId(userInfo: D): String

    companion object {
        private const val USER_TOKEN = "com.bonepeople.android.key.USER_TOKEN"
        private const val USER_ID = "com.bonepeople.android.key.USER_ID"
        private const val USER_INFO = "com.bonepeople.android.key.USER_INFO"
    }
}