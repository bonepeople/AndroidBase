package com.bonepeople.android.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.shade.Protector
import com.bonepeople.android.base.viewbinding.ViewBindingActivity
import com.bonepeople.android.base.databinding.ActivityStandardBinding
import com.bonepeople.android.widget.ActivityHolder
import com.bonepeople.android.widget.activity.result.IntentResult
import com.bonepeople.android.widget.activity.result.launch
import java.util.*

class StandardActivity : ViewBindingActivity<ActivityStandardBinding>() {

    override fun initView() {

    }

    override fun initData(savedInstanceState: Bundle?) {
        val fragmentKey = Protector.protect { intent.getStringExtra(FRAGMENT_KEY) ?: "default_key" }
        var fragment = supportFragmentManager.findFragmentByTag(fragmentKey)
        if (fragment == null) {
            fragment = getFragment(fragmentKey)
            supportFragmentManager.beginTransaction().add(views.fragmentContainerViewActivity.id, fragment, fragmentKey).commit()
        }
        onInit?.invoke(savedInstanceState)
    }

    override fun setStatusBar() {
        statusBar?.invoke(this) ?: run { super.setStatusBar() }
    }

    override fun toString(): String {
        val hash = System.identityHashCode(this).toString(16)
        val fragmentKey = Protector.protect { intent.getStringExtra(FRAGMENT_KEY) ?: "default_key" }
        val fragment = supportFragmentManager.findFragmentByTag(fragmentKey)
        return "StandardActivity@$hash => $fragment"
    }

    companion object {
        private const val FRAGMENT_KEY = "FRAGMENT_KEY"
        private val fragmentContainer = HashMap<String, Fragment>()
        var statusBar: (StandardActivity.() -> Unit)? = null
        var onInit: ((savedInstanceState: Bundle?) -> Unit)? = null

        /**
         * 通过[startActivity]方法打开一个新的页面并加载提供的[Fragment]
         */
        fun open(fragment: Fragment) = ActivityHolder.getTopActivity()?.let {
            val fragmentKey = UUID.randomUUID().toString()
            fragmentContainer[fragmentKey] = fragment
            Intent(it, this::class.java.enclosingClass).run {
                putExtra(FRAGMENT_KEY, fragmentKey)
                it.startActivity(this)
            }
        }

        /**
         * 通过[startActivityForResult]方法打开一个新的页面并加载提供的[Fragment]
         * @return 返回[IntentResult]，用于处理[Activity]返回的结果
         */
        fun call(fragment: Fragment): IntentResult {
            val activity = ActivityHolder.getTopActivity()
            return if (activity == null) {
                throw IllegalStateException("No Activity to call startActivityForResult")
            } else {
                val fragmentKey = UUID.randomUUID().toString()
                fragmentContainer[fragmentKey] = fragment
                Intent(activity, this::class.java.enclosingClass).apply {
                    putExtra(FRAGMENT_KEY, fragmentKey)
                }.launch()
            }
        }

        private fun getFragment(fragmentKey: String): Fragment {
            return fragmentContainer.remove(fragmentKey) ?: ErrorFragment()
        }
    }
}