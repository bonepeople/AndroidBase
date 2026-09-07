package com.bonepeople.android.base.foundation

import com.bonepeople.android.base.manager.UserStateEvent
import com.bonepeople.android.widget.CoroutinesHolder
import com.bonepeople.android.widget.util.AppData
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.event.AppEvent
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class QuickUserManager<D> {
    private val userClass: Class<*> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    private val defaultUserInfo: D = userClass.newInstance() as D
    private val userData: AppData = AppData.create("com.bonepeople.android.base.manager.BaseUserManager")

    val userId: String
        get() = userData.getStringSync(USER_ID)
    val userInfo: D
        get() = AppGson.defaultGson.fromJson<D>(userData.getStringSync(USER_INFO), userClass) ?: defaultUserInfo

    val isLogin: Boolean
        get() {
            return userId.isNotBlank()
        }

    suspend fun login(newInfo: D) {
        CoroutinesHolder.runSerial(SERIAL_NAME) {
            val event = if (userData.getString(USER_ID).isBlank()) UserStateEvent.LOGIN else UserStateEvent.UPDATE
            userData.putString(USER_INFO, AppGson.toJson(newInfo))
            userData.putString(USER_ID, resolveUserId(newInfo))
            AppEvent.post(event)
        }
    }

    suspend fun logout() {
        CoroutinesHolder.runSerial(SERIAL_NAME) {
            if (userData.getString(USER_ID).isBlank()) return@runSerial

            userData.putString(USER_TOKEN, "")
            userData.putString(USER_INFO, "")
            userData.putString(USER_ID, resolveUserId(defaultUserInfo))
            AppEvent.post(UserStateEvent.LOGOUT)
        }
    }

    suspend fun getToken(): String {
        return CoroutinesHolder.runSerial(SERIAL_NAME) {
            userData.getString(USER_TOKEN)
        }
    }

    suspend fun setToken(value: String) {
        CoroutinesHolder.runSerial(SERIAL_NAME) {
            userData.putString(USER_TOKEN, value)
        }
    }

    abstract fun resolveUserId(userInfo: D): String

    companion object {
        private const val SERIAL_NAME = "com.bonepeople.android.base.manager.BaseUserManager.serial"
        private const val USER_TOKEN = "com.bonepeople.android.key.USER_TOKEN"
        private const val USER_ID = "com.bonepeople.android.key.USER_ID"
        private const val USER_INFO = "com.bonepeople.android.key.USER_INFO"
    }
}