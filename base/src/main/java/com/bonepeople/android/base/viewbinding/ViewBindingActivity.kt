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
 * Activity抽象类
 * + 包含自动实例化的ViewBinding和一个基础的LoadingDialog。
 * + 泛型参数中需要传入当前界面的ViewBinding，该ViewBinding会在界面初始化的时候实例化并加载到页面中，之后以views变量的方式供子类使用。
 * + LoadingDialog采用懒加载，不使用不会占用资源。
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
     * 分发触摸事件，在分发的同时判断是否需要隐藏软键盘并执行响应操作
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
     * 默认使用ImmersionBar将状态栏设置为透明状态
     * + 需要修改默认样式的时候请重写该函数
     */
    protected open fun setStatusBar() {
        immersionBar {
            keyboardEnable(true)
        }
    }

    /**
     * 判断是否需要隐藏键盘
     */
    open var needHideKeyboard: (focusedView: View?, motionEvent: MotionEvent) -> Boolean = { focusedView, motionEvent ->
        AppKeyboard.needHideKeyboard(focusedView, motionEvent)
    }

    /**
     * 初始化界面
     * + 会在onCreate中被调用
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     * + 会在onCreate中被调用
     */
    protected open fun initData(savedInstanceState: Bundle?) {}
}