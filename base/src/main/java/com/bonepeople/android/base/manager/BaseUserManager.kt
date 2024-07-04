package com.bonepeople.android.base.manager

import androidx.shade.migrate.DataMigrateInfo
import androidx.shade.migrate.DataMigrateUtil
import com.bonepeople.android.base.util.CoroutineExtension.launchOnIO
import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil
import com.bonepeople.android.widget.util.AppData
import com.bonepeople.android.widget.util.AppGson
import com.bonepeople.android.widget.util.AppStorage
import kotlinx.coroutines.runBlocking
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseUserManager<D> {
    private val userClass: Class<*> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    private val defaultUserInfo: D = userClass.newInstance() as D
    private val userData by lazy {
        val field = AppData.create("com.bonepeople.android.base.manager.BaseUserManager")
        runBlocking {
            DataMigrateUtil.migrate(
                dataId = "com.bonepeople.android.base.manager.BaseUserManager",
                migrateList = listOf(
                    object : DataMigrateInfo {
                        override val range: IntRange = 0..1
                        override val action: suspend () -> Unit = {
                            field.putString(USER_TOKEN, AppStorage.getString(USER_TOKEN))
                            field.putString(USER_ID, AppStorage.getString(USER_ID))
                            field.putString(USER_INFO, AppStorage.getString(USER_INFO))
                            launchOnIO {
                                AppStorage.remove(USER_TOKEN)
                                AppStorage.remove(USER_ID)
                                AppStorage.remove(USER_INFO)
                            }
                        }
                    },
                )
            )
        }
        field
    }
    var token: String = USER_TOKEN
        get() {
            if (field == USER_TOKEN) {
                field = userData.getStringSync(USER_TOKEN)
            }
            return field
        }
        set(value) {
            userData.putStringSync(USER_TOKEN, value)
            field = value
        }
    val userId: String
        get() = userData.getStringSync(USER_ID)
    val userInfo: D
        get() = AppGson.defaultGson.fromJson<D>(userData.getStringSync(USER_INFO), userClass) ?: defaultUserInfo

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