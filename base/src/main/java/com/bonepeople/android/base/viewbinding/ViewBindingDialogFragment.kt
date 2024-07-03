package com.bonepeople.android.base.viewbinding

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.databinding.DialogContainerBinding
import com.bonepeople.android.widget.util.AppView.gone
import kotlinx.coroutines.delay
import java.lang.reflect.ParameterizedType

/**
 * 通用的DialogFragment
 * + 根据DialogFragment的使用特性对展示和隐藏进行了封装，符合常规使用逻辑，并且避免了部分异常情况
 * + 添加了ViewBinding的支持，方便用户实现简单的Dialog
 */
abstract class ViewBindingDialogFragment<V : ViewBinding> : DialogFragment() {
    private var dialogFragmentManager: FragmentManager? = null
    private var dialogFragmentTag: String = ""
    private val container by lazy { DialogContainerBinding.inflate(layoutInflater) }

    @Suppress("UNCHECKED_CAST")
    protected val views by lazy {
        Protector.protect {
            val superclass = javaClass.genericSuperclass as ParameterizedType
            val subclass = superclass.actualTypeArguments[0] as Class<*>
            val method = subclass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
            method.invoke(null, layoutInflater, container.root, true) as V
        }
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, savedInstanceState: Bundle?): View {
        val params = views.root.layoutParams
        if (params.width != ViewGroup.LayoutParams.MATCH_PARENT)
            container.viewHorizontal.gone()
        if (params.height != ViewGroup.LayoutParams.MATCH_PARENT)
            container.viewVertical.gone()
        return container.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.run { setBackgroundDrawable(GradientDrawable()) }
        initView()
    }

    override fun onDestroyView() {
        (container.root.parent as? ViewGroup)?.removeView(container.root)
        super.onDestroyView()
    }

    /**
     * 设置用于显示Dialog的FragmentManager及Tag
     */
    fun setManagerAndTag(manager: FragmentManager, tag: String) {
        dialogFragmentManager = manager
        dialogFragmentTag = tag
    }

    /**
     * 显示Dialog
     * + 对于已经显示的Dialog，重复调用此函数不会进行任何操作
     * + 在显示之前需要调用[setManagerAndTag]方法设置FragmentManager
     */
    @CallSuper
    open fun show(lifecycleOwner: LifecycleOwner? = null) {
        require(dialogFragmentManager != null) { "需要通过 setManagerAndTag 方法设置 FragmentManager 及 Tag" }
        if (isAdded) return
        lifecycleOwner?.lifecycleScope?.launchWhenResumed {
            delay(177)
            show(dialogFragmentManager!!, dialogFragmentTag)
        } ?: kotlin.runCatching {
            if (!dialogFragmentManager!!.isStateSaved) {
                show(dialogFragmentManager!!, dialogFragmentTag)
            }
        }
    }

    /**
     * 关闭Dialog
     * + 调用此函数的时候会对Dialog状态进行判断，未在前台的时候暂时不关闭，待界面返回前台的时候关闭
     */
    @CallSuper
    override fun dismiss() {
        if (lifecycle.currentState == Lifecycle.State.INITIALIZED) return
        lifecycleScope.launchWhenResumed {
            delay(177)
            super.dismiss()
        }
    }

    /**
     * 初始化界面
     * + 会在onViewCreated中被调用
     */
    protected abstract fun initView()
}