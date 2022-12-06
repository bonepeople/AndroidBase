package com.bonepeople.android.base.example

import com.bonepeople.android.widget.util.AppLog
import com.bonepeople.android.widget.util.AppToast
import com.google.gson.annotations.SerializedName

class SimpleHttpResponse<R> {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("msg")
    var msg: String = ""

    @SerializedName("data")
    var data: R? = null
    private var logout: Boolean = false

    fun checkLoginState() = apply {
        if (code in 20..29) {
            // BaseUserManager.logout()
            AppToast.show("登陆信息已过期，请重新登陆")
            logout = true
        }
    }

    fun isSuccessful(): Boolean {
        return code == SUCCESSFUL && data != null
    }

    suspend fun onSuccess(action: suspend (value: R) -> Unit) = apply {
        if (logout) return this
        kotlin.runCatching {
            if (isSuccessful()) {
                action(data!!)
            }
        }.onFailure {
            code = FAILURE
            msg = it.message.toString()
            AppLog.error("com.bonepeople.android.base.example.SimpleHttpResponse.onSuccess", it)
            AppToast.show(it.message)
        }
    }

    suspend fun onFailure(action: suspend (code: Int, msg: String) -> Unit) = apply {
        if (logout) return this
        if (code == CANCEL) return this
        kotlin.runCatching {
            if (!isSuccessful()) {
                action(code, msg)
            }
        }.onFailure {
            AppLog.error("com.bonepeople.android.base.example.SimpleHttpResponse.onFailure", it)
            AppToast.show(it.message)
        }
    }

    fun defaultFailure() = apply {
        if (logout) return this
        if (code == CANCEL) return this
        if (!isSuccessful()) {
            AppToast.show(msg)
        }
    }

    companion object {
        const val SUCCESSFUL = 1
        const val FAILURE = 0
        const val CANCEL = -1
    }
}