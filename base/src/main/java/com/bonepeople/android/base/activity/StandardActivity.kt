package com.bonepeople.android.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bonepeople.android.base.ViewBindingActivity
import com.bonepeople.android.base.databinding.ActivityStandardBinding
import java.util.*

class StandardActivity : ViewBindingActivity<ActivityStandardBinding>() {

    override fun initView() {

    }

    override fun initData(savedInstanceState: Bundle?) {
        val fragmentKey = intent.getStringExtra(FRAGMENT_KEY) ?: "default_key"
        var fragment = supportFragmentManager.findFragmentByTag(fragmentKey)
        if (fragment == null) {
            fragment = getFragment(fragmentKey)
            supportFragmentManager.beginTransaction().add(views.fragmentContainerViewActivity.id, fragment, fragmentKey).commit()
        }
    }

    companion object {
        private const val FRAGMENT_KEY = "FRAGMENT_KEY"
        private val fragmentContainer = HashMap<String, Fragment>()

        fun open(activity: Activity?, fragment: Fragment) = activity?.let {
            val fragmentKey = UUID.randomUUID().toString()
            fragmentContainer[fragmentKey] = fragment
            Intent(it, this::class.java.enclosingClass).run {
                putExtra(FRAGMENT_KEY, fragmentKey)
                it.startActivity(this)
            }
        }

        private fun getFragment(fragmentKey: String): Fragment {
            return fragmentContainer.remove(fragmentKey) ?: ErrorFragment()
        }
    }
}