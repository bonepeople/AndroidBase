package com.bonepeople.android.base.manager

import com.bonepeople.android.widget.util.event.AppEventData

enum class UserStateEvent : AppEventData {
    LOGIN,
    LOGOUT,
    UPDATE,
}