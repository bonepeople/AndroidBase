package com.bonepeople.android.base

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 通用的DialogFragment
 * + 根据DialogFragment的使用特性对展示和隐藏进行了封装，符合常规使用逻辑，并且避免了部分异常情况
 * + 添加了ViewBinding的支持，方便用户实现简单的Dialog
 */
abstract class ViewBindingDialogFragment<V : ViewBinding>(private val manager: FragmentManager) : DialogFragment() {
    private var closing = false

    @Suppress("UNCHECKED_CAST")
    protected val views by lazy {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val subclass = superclass.actualTypeArguments[0] as Class<*>
        val method = subclass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater) as V
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = views.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.run { setBackgroundDrawable(GradientDrawable()) }
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (closing) {
            dismiss()
        }
    }

    override fun onDestroyView() {
        (views.root.parent as? ViewGroup)?.removeView(views.root)
        super.onDestroyView()
    }

    /**
     * 显示Dialog
     * + 对于已经显示的Dialog，重复调用此函数不会进行任何操作
     */
    @CallSuper
    open fun show() {
        if (isAdded) return
        closing = false
        show(manager, null)
    }

    /**
     * 关闭Dialog
     * + 调用此函数的时候会对Dialog状态进行判断，未在前台的时候暂时不关闭，待界面返回前台的时候关闭
     */
    @CallSuper
    override fun dismiss() {
        if (isResumed) {
            super.dismiss()
        } else {
            closing = true
        }
    }

    /**
     * 初始化界面
     * + 会在onViewCreated中被调用
     */
    protected abstract fun initView()
}