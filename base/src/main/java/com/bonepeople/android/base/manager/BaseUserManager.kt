package com.bonepeople.android.base.manager

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil
import com.bonepeople.android.widget.util.AppData
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppStorage
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseUserManager<D> {
    private val userClass: Class<*> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    private val defaultUserInfo: D = userClass.newInstance() as D
    private val userData = AppData.create("com.bonepeople.android.base.manager.BaseUserManager")

    @Deprecated("Since AppStorage has been deprecated, replace its usage with AppData. This property will be removed from version 1.7.0.")
    private val userStorage = AppStorage
    var token: String = USER_TOKEN
        get() {
            if (field == USER_TOKEN) {
                field = userData.getStringSync(USER_TOKEN).ifEmpty {
                    val data = userStorage.getString(USER_TOKEN)
                    userData.putStringSync(USER_TOKEN, data)
                    data
                }
            }
            return field
        }
        set(value) {
            userData.putStringSync(USER_TOKEN, value)
            field = value
        }
    val userId: String
        get() = userData.getStringSync(USER_ID).ifEmpty {
            val data = userStorage.getString(USER_ID)
            userData.putStringSync(USER_ID, data)
            data
        }
    val userInfo: D
        get() = AppGson.defaultGson.fromJson<D>(userData.getStringSync(USER_INFO).ifEmpty {
            val data = userStorage.getString(USER_INFO)
            userData.putStringSync(USER_INFO, data)
            data
        }, userClass) ?: defaultUserInfo

    val isLogin: Boolean
        get() {
            return userId.isNotBlank()
        }

    fun login(newInfo: D) {
        val login = !isLogin
        userData.putStringSync(USER_INFO, AppGson.toJson(newInfo))
        userData.putStringSync(USER_ID, resolveUserId(newInfo))
        if (login) {
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_LOGIN)
        } else {
            LocalBroadcastUtil.sendBroadcast(BroadcastAction.USER_UPDATE)
        }
    }

    fun logout() {
        if (isLogin) {
            token = ""
            userData.putStringSync(USER_INFO, "")
            userData.putStringSync(USER_ID, resolveUserId(defaultUserInfo))
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