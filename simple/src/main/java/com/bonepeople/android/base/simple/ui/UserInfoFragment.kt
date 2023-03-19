package com.bonepeople.android.base.simple.ui

import android.os.Bundle
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.manager.BroadcastAction
import com.bonepeople.android.base.simple.data.UserInfo
import com.bonepeople.android.base.simple.databinding.FragmentUserInfoBinding
import com.bonepeople.android.base.simple.global.UserManager
import com.bonepeople.android.localbroadcastutil.LocalBroadcastHelper
import com.bonepeople.android.widget.util.AppRandom
import com.bonepeople.android.widget.util.AppView.singleClick

class UserInfoFragment : ViewBindingFragment<FragmentUserInfoBinding>() {
    override fun initView() {
        views.titleView.textViewTitleName.text = "UserManager"
        views.buttonLogin.singleClick {
            UserManager.token = AppRandom.randomString(16)
            val userInfo = UserInfo().apply { id = AppRandom.randomInt(1..100).toString();name = AppRandom.randomString(4) }
            UserManager.login(userInfo)
        }
        views.buttonLogout.singleClick { UserManager.logout() }
        updateView()
    }

    override fun initData(savedInstanceState: Bundle?) {
        LocalBroadcastHelper.register(viewLifecycleOwner, BroadcastAction.USER_LOGIN, BroadcastAction.USER_LOGOUT, BroadcastAction.USER_UPDATE) { updateView() }
    }

    private fun updateView() {
        views.textViewStatus.text = UserManager.isLogin.toString()
        views.textViewToken.text = UserManager.token
        views.textViewId.text = UserManager.userId
        views.textViewName.text = UserManager.userInfo.name
    }
}