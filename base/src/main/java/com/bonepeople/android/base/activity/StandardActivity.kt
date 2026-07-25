package com.bonepeople.android.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.shade.Protector
import com.bonepeople.android.base.databinding.ActivityStandardBinding
import com.bonepeople.android.base.viewbinding.ViewBindingActivity2
import com.bonepeople.android.widget.ActivityHolder
import com.bonepeople.android.widget.activity.result.IntentResult
import com.bonepeople.android.widget.activity.result.launch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class StandardActivity : ViewBindingActivity2<ActivityStandardBinding>() {
    private var requestKey: String? = null
    private var launchRequest: LaunchRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val key = getRequestKey()
        requestKey = key
        launchRequest = requestContainer[key]
        super.onCreate(savedInstanceState)
    }

    override fun setStatusBar() {
        launchRequest?.config?.statusBar?.invoke(this) ?: statusBar?.invoke(this) ?: super.setStatusBar()
    }

    override fun initView() {
        // Initialize views here if needed
    }

    override fun initData(savedInstanceState: Bundle?) {
        val key = requestKey ?: DEFAULT_KEY
        var fragment = supportFragmentManager.findFragmentByTag(key)
        if (fragment == null) {
            fragment = launchRequest?.fragment ?: ErrorFragment()
            launchRequest?.fragment = null
            supportFragmentManager.beginTransaction().add(views.fragmentContainerViewActivity.id, fragment, key).commit()
        }
        launchRequest?.config?.onInit?.invoke(this, savedInstanceState) ?: onInit?.invoke(savedInstanceState)
    }

    override fun onDestroy() {
        if (isFinishing) {
            requestKey?.let(requestContainer::remove)
        }
        super.onDestroy()
    }

    override fun toString(): String {
        val hash = System.identityHashCode(this).toString(16)
        val key = getRequestKey()
        val fragment = supportFragmentManager.findFragmentByTag(key)
        return "StandardActivity@$hash => $fragment"
    }

    private fun getRequestKey(): String {
        return Protector.protect { intent.getStringExtra(FRAGMENT_KEY) ?: DEFAULT_KEY }
    }

    private class LaunchRequest(
        var fragment: Fragment?,
        val config: StandardActivityConfig,
    )

    companion object {
        private const val FRAGMENT_KEY = "FRAGMENT_KEY"
        private const val DEFAULT_KEY = "default_key"
        private val requestContainer = ConcurrentHashMap<String, LaunchRequest>()
        var statusBar: (StandardActivity.() -> Unit)? = null
        var onInit: ((savedInstanceState: Bundle?) -> Unit)? = null

        /**
         * Opens a new page via [startActivity], loads the given [Fragment], and applies [config] to this request only.
         *
         * The configuration is kept only in the current process and cannot be restored after process death.
         */
        @JvmOverloads
        fun open(
            fragment: Fragment,
            config: StandardActivityConfig = StandardActivityConfig.Empty,
        ) {
            val activity = requireTopActivity()
            val requestKey = UUID.randomUUID().toString()
            requestContainer[requestKey] = LaunchRequest(fragment, config)
            try {
                Intent(activity, StandardActivity::class.java).run {
                    putExtra(FRAGMENT_KEY, requestKey)
                    activity.startActivity(this)
                }
            } catch (throwable: Throwable) {
                requestContainer.remove(requestKey)
                throw throwable
            }
        }

        /**
         * Opens a new page via [startActivityForResult], loads the given [Fragment], and applies [config] to this request only.
         *
         * The configuration is kept only in the current process and cannot be restored after process death.
         *
         * @return [IntentResult] for handling the result returned from the [Activity]
         */
        @JvmOverloads
        fun call(
            fragment: Fragment,
            config: StandardActivityConfig = StandardActivityConfig.Empty,
        ): IntentResult {
            val activity = requireTopActivity()
            val requestKey = UUID.randomUUID().toString()
            requestContainer[requestKey] = LaunchRequest(fragment, config)
            return try {
                Intent(activity, StandardActivity::class.java).apply {
                    putExtra(FRAGMENT_KEY, requestKey)
                }.launch()
            } catch (throwable: Throwable) {
                requestContainer.remove(requestKey)
                throw throwable
            }
        }

        private fun requireTopActivity(): Activity {
            return checkNotNull(ActivityHolder.getTopActivity()) {
                "No Activity available to launch StandardActivity"
            }
        }
    }
}