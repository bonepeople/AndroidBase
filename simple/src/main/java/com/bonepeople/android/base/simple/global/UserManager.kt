package com.bonepeople.android.base.simple.global

import com.bonepeople.android.base.manager.BaseUserManager
import com.bonepeople.android.base.simple.data.UserInfo

object UserManager : BaseUserManager<UserInfo>() {
    override fun resolveUserId(userInfo: UserInfo) = userInfo.id
}