package com.bonepeople.android.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.CoroutineLifecycleObserver
import com.bonepeople.android.base.view.CustomLoadingDialog
import com.bonepeople.android.widget.util.AppKeyboard
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Abstract Activity class
 * + Includes automatically instantiated ViewBinding and a basic LoadingDialog.
 * + The generic parameter should specify the ViewBinding of the current screen. This ViewBinding will be instantiated and loaded during initialization,
 *   and can be accessed via the `views` variable in subclasses.
 * + LoadingDialog uses lazy initialization, so it won’t consume resources if not used.
 */
abstract class ViewBindingActivity<V : ViewBinding> : AppCompatActivity(), CoroutineScope {
    @Deprecated("CoroutineScope will no longer be supported. This method will be removed from version 1.7.0.")
    override val coroutineContext: CoroutineContext by lazy {
        (Dispatchers.Main + Job()).also {
            lifecycle.addObserver(CoroutineLifecycleObserver(it))
        }
    }

    @Deprecated(
        message = "Use lifecycleScope.launch(context, start, block) instead. This method will be removed from version 1.7.0.",
        replaceWith = ReplaceWith("lifecycleScope.launch(context, start, block)", "androidx.lifecycle.lifecycleScope", "kotlinx.coroutines.launch")
    )
    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = lifecycleScope.launch(context, start, block)

    @Suppress("UNCHECKED_CAST")
    protected val views: V by lazy {
        Protector.protect {
            val superclass = javaClass.genericSuperclass as ParameterizedType
            val subclass = superclass.actualTypeArguments[0] as Class<*>
            val method = subclass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            method.invoke(null, layoutInflater) as V
        }
    }
    protected val loadingDialog: CustomLoadingDialog by lazy {
        val tag = "ViewBindingActivity.loadingDialog"
        val dialog: CustomLoadingDialog = supportFragmentManager.findFragmentByTag(tag) as? CustomLoadingDialog ?: CustomLoadingDialog()
        dialog.also { it.setManagerAndTag(supportFragmentManager, tag) }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        setStatusBar()
        initView()
        initData(savedInstanceState)
    }

    /**
     * Dispatches touch events. Also checks if the keyboard should be hidden and performs the corresponding action.
     */
    @CallSuper
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (needHideKeyboard(currentFocus, ev)) {
                AppKeyboard.hideKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Sets the status bar to transparent using ImmersionBar by default.
     * + Override this method to customize the default style.
     */
    protected open fun setStatusBar() {
        immersionBar {
            keyboardEnable(true)
        }
    }

    /**
     * Determines whether the keyboard should be hidden.
     */
    open var needHideKeyboard: (focusedView: View?, motionEvent: MotionEvent) -> Boolean = { focusedView, motionEvent ->
        AppKeyboard.needHideKeyboard(focusedView, motionEvent)
    }

    /**
     * Initializes the view.
     * + Called in onCreate.
     */
    protected abstract fun initView()

    /**
     * Initializes data.
     * + Called in onCreate.
     */
    protected open fun initData(savedInstanceState: Bundle?) {}
}