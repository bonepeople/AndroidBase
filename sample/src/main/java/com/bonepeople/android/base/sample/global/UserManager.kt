package com.bonepeople.android.base.sample.global

import com.bonepeople.android.base.manager.BaseUserManager
import com.bonepeople.android.base.sample.data.UserInfo

object UserManager : BaseUserManager<UserInfo>() {
    override fun resolveUserId(userInfo: UserInfo) = userInfo.id
}