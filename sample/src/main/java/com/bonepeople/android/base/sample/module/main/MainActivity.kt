package com.bonepeople.android.base.sample.module.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bonepeople.android.base.activity.StandardActivity
import com.bonepeople.android.base.sample.databinding.ActivityMainBinding
import com.bonepeople.android.base.sample.module.flow.FlowFragment
import com.bonepeople.android.base.sample.module.recycleradapter.ProductListFragment
import com.bonepeople.android.base.sample.module.test.TestFragment
import com.bonepeople.android.base.sample.module.usermanager.UserInfoFragment
import com.bonepeople.android.base.viewbinding.ViewBindingActivity2
import com.bonepeople.android.widget.ActivityHolder
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.launch

/** Sample entry page and Activity code structure reference. */
class MainActivity : ViewBindingActivity2<ActivityMainBinding>() {
    private var currentIndex = 1

    override fun initView() {
        // Handle back press; show a confirm dialog here if needed
        // onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        //     override fun handleOnBackPressed() {
        //         finish()
        //     }
        // })
        // views.titleView.onBackClick { onBackPressedDispatcher.onBackPressed() }
        views.titleView.title = "BaseTestActivity"
        // views.titleView.onActionClick { AppToast.show("Click Action") }
        // Sample module entry points
        views.buttonUserManager.singleClick { StandardActivity.open(UserInfoFragment()) }
        views.buttonProductList.singleClick { StandardActivity.open(ProductListFragment()) }
        views.buttonFlow.singleClick { StandardActivity.open(FlowFragment()) }
        views.buttonTest.singleClick { StandardActivity.open(TestFragment()) }
        // views.buttonSubmit.singleClick { submit() }
    }

    override fun initData(savedInstanceState: Bundle?) {
        // Read launch args from Intent
        currentIndex = intent.getIntExtra(CURRENT_INDEX, 1)
        // Restore state after config change or process death
        savedInstanceState?.let { currentIndex = it.getInt(CURRENT_INDEX, currentIndex) }
        loadData()
    }

    /**
     * Called when this Activity is already running and receives a new Intent.
     * Use with singleTask / singleTop: e.g. [open] brings back this page instead of creating a new one.
     * onCreate / initData will NOT run again — read the new Intent here and refresh the UI.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIndex = intent.getIntExtra(CURRENT_INDEX, 1)
        updateData()
    }

    /**
     * Called before the Activity may be destroyed (screen rotation, low memory, process death).
     * Save temporary UI state here; restore it in initData from savedInstanceState.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_INDEX, currentIndex)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        // Release resources here if needed
        super.onDestroy()
    }

    private fun loadData() {
        lifecycleScope.launch {
            simpleLoadingDialog.show()
            try {
                // fetchData()
                updateData()
                // delay(2000) // simulate network delay if needed
            } finally {
                simpleLoadingDialog.dismiss()
            }
        }
        // Prefer ViewModel loading state + simpleLoadingDialog.switchShow() in production
    }

    private fun updateData() {
        // Refresh UI based on currentIndex or other data
    }

    private fun submit() {
        // Submit form or call API
    }

    companion object {
        private const val CURRENT_INDEX = "currentIndex"

        /** Opens this Activity from other pages; call directly to navigate here. */
        fun open() {
            ActivityHolder.getTopActivity()?.let { activity: Activity ->
                val intent = Intent(activity, MainActivity::class.java)
                // For singleTask: return to existing instance and clear Activities above it
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity.startActivity(intent)
            }
        }
    }
}