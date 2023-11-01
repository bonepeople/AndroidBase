package com.bonepeople.android.base.exception

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.HashMap

class ExceptionInfo {
    @SerializedName("time")
    var time = ""

    @SerializedName("timestamp")
    var timestamp = 0L

    @SerializedName("thread")
    var thread = ""

    @SerializedName("appVersion")
    var appVersion = ""

    @SerializedName("osVersion")
    var osVersion = ""

    @SerializedName("manufacturer")
    var manufacturer = ""

    @SerializedName("model")
    var model = ""

    @SerializedName("systemAbi")
    var systemAbi = ""

    @SerializedName("appAbi")
    var appAbi = "unknown"

    @SerializedName("message")
    var message = ""

    @SerializedName("stack")
    var stack = LinkedList<String>()

    @SerializedName("extra")
    var extra = HashMap<String, String>()
}