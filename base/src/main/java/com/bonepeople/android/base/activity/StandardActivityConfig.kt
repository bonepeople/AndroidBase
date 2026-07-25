package com.bonepeople.android.base.activity

import android.os.Bundle

/**
 * Reusable configuration for one or more [StandardActivity] launch requests.
 *
 * The configuration passed to [StandardActivity.open] or [StandardActivity.call] is kept only in the current process and is not saved to the launch
 * [android.content.Intent] or instance state. Request-specific callbacks are unavailable if the Activity is restored after process death.
 *
 * Callbacks should not capture Activity, Fragment, or View instances because a shared configuration may live for the entire application process.
 */
class StandardActivityConfig(
    val statusBar: (StandardActivity.() -> Unit)? = null,
    val onInit: (StandardActivity.(savedInstanceState: Bundle?) -> Unit)? = null,
) {
    companion object {
        val Empty = StandardActivityConfig()
    }
}