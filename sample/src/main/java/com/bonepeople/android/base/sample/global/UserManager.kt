package com.bonepeople.android.base.sample.global

import com.bonepeople.android.base.foundation.QuickUserManager
import com.bonepeople.android.base.sample.global.data.UserInfo

object UserManager : QuickUserManager<UserInfo>() {
    override fun resolveUserId(userInfo: UserInfo) = userInfo.id
}