package com.bonepeople.android.base.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bonepeople.android.base.viewbinding.ViewBindingActivity
import com.bonepeople.android.base.databinding.ActivitySimpleBinding
import com.bonepeople.android.localbroadcastutil.LocalBroadcastHelper
import com.bonepeople.android.widget.ActivityHolder
import com.bonepeople.android.widget.util.AppView.show
import com.bonepeople.android.widget.util.AppView.singleClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimpleViewBindingActivity : ViewBindingActivity<ActivitySimpleBinding>() {
    private var currentIndex = 1

    override fun initView() {
        views.titleView.imageViewTitleBack.run {
            singleClick { onBackPressed() }
            show()
        }
        views.titleView.textViewTitleName.text = "SimpleActivity"
        views.buttonSubmit.singleClick { submit() }
    }

    override fun initData(savedInstanceState: Bundle?) {
        LocalBroadcastHelper().setLifecycleOwner(this).setReceiver(Receiver()).addAction("EXAMPLE").register()
        currentIndex = intent.getIntExtra(CURRENT_INDEX, 1)
        savedInstanceState?.let {
            currentIndex = it.getInt(CURRENT_INDEX)
        }
        launch {
            loadingDialog.show()
            //fetchData
            updateData()
            delay(2000)
            loadingDialog.dismiss()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIndex = intent.getIntExtra(CURRENT_INDEX, 1)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_INDEX, currentIndex)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //...
        super.onBackPressed()
    }

    override fun onDestroy() {
        //...
        super.onDestroy()
    }

    private fun updateData() {
        //...
    }

    private fun submit() {
        //...
    }

    companion object {
        private const val CURRENT_INDEX = "currentIndex"
        fun open(index: Int) = ActivityHolder.getTopActivity()?.let {
            Intent(it, this::class.java.enclosingClass).run {
                putExtra(CURRENT_INDEX, index)
                it.startActivity(this)
            }
        }
    }

    private inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "EXAMPLE" -> {
                    //...
                }
            }
        }
    }
}