package com.bonepeople.android.base.sample.module.test.usermanager

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bonepeople.android.base.manager.UserStateEvent
import com.bonepeople.android.base.sample.R
import com.bonepeople.android.base.sample.databinding.FragmentUserInfoBinding
import com.bonepeople.android.base.sample.global.UserManager
import com.bonepeople.android.base.sample.global.data.UserInfo
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.widget.util.AppRandom
import com.bonepeople.android.widget.util.AppView.singleClick
import com.bonepeople.android.widget.util.event.AppEvent

class UserInfoFragment : ViewBindingFragment<FragmentUserInfoBinding>() {
    override fun initView() {
        views.titleView.title = "UserManager"
        views.buttonLogin.singleClick {
            UserManager.token = AppRandom.randomString(16)
            val userInfo = UserInfo().apply { id = AppRandom.randomInt(1..100).toString();name = AppRandom.randomString(4) }
            UserManager.login(userInfo)
        }
        views.buttonLogout.singleClick { UserManager.logout() }
        updateView()
    }

    override fun initData(savedInstanceState: Bundle?) {
        AppEvent.register(viewLifecycleOwner) { event ->
            if (event is UserStateEvent) {
                updateView()
            }
        }
    }

    private fun updateView() {
        val loggedIn = UserManager.isLogin
        views.textViewStatus.text = if (loggedIn) "Logged in" else "Not logged in"
        views.textViewStatus.setTextColor(ContextCompat.getColor(requireContext(), if (loggedIn) R.color.secondary else R.color.textSecondary))
        views.textViewToken.text = UserManager.token.ifBlank { "—" }
        views.textViewId.text = UserManager.userId.ifBlank { "—" }
        views.textViewName.text = UserManager.userInfo.name.ifBlank { "—" }
        views.buttonLogin.text = if (loggedIn) "Update" else "Login"
    }
}