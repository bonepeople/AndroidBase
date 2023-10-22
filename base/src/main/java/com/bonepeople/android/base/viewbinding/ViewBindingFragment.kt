package com.bonepeople.android.base.viewbinding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.CoroutineLifecycleObserver
import com.bonepeople.android.base.view.CustomLoadingDialog
import com.bonepeople.android.shade.Protector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

/**
 * Fragment抽象类
 * + 包含自动实例化的ViewBinding、协程作用域和一个基础的LoadingDialog。
 * + 泛型参数中需要传入当前界面的ViewBinding，该ViewBinding会在界面初始化的时候实例化并加载到页面中，之后以views变量的方式供子类使用。
 * + 协程和LoadingDialog采用懒加载，不使用不会占用资源。
 */
abstract class ViewBindingFragment<V : ViewBinding> : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy {
        (Dispatchers.Main + Job()).also {
            viewLifecycleOwner.lifecycle.addObserver(CoroutineLifecycleObserver(it))
        }
    }
    private val onBackListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

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
        val tag = "ViewBindingFragment.loadingDialog"
        val dialog: CustomLoadingDialog = childFragmentManager.findFragmentByTag(tag) as? CustomLoadingDialog ?: CustomLoadingDialog()
        dialog.also { it.setManagerAndTag(childFragmentManager, tag) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return views.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initData(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackListener)
    }

    open fun onBackPressed() {
        onBackListener.isEnabled = false
        requireActivity().onBackPressed()
    }

    /**
     * 初始化界面
     * + 会在onViewCreated中被调用
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     * + 会在onViewCreated中被调用
     */
    protected open fun initData(savedInstanceState: Bundle?) {}
}