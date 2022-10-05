package com.bonepeople.android.base.simple.global

import com.bonepeople.android.base.manager.BaseUserManager
import com.bonepeople.android.base.simple.data.UserInfo

object UserManager : BaseUserManager<UserInfo>() {
    override val defaultUserInfo = UserInfo().apply { id = "nothing" }
    override fun resolveUserId(userInfo: UserInfo) = userInfo.id
}