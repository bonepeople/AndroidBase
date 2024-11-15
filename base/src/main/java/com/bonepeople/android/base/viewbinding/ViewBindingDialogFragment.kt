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

    fun setManagerAndTag(manager: FragmentManager, tag: String) {
        dialogFragmentManager = manager
        dialogFragmentTag = tag
    }

    @CallSuper
    open fun show(lifecycleOwner: LifecycleOwner? = null) {
        require(dialogFragmentManager != null) { "need setManagerAndTag" }
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

    @CallSuper
    override fun dismiss() {
        if (lifecycle.currentState == Lifecycle.State.INITIALIZED) return
        lifecycleScope.launchWhenResumed {
            delay(177)
            super.dismiss()
        }
    }

    protected abstract fun initView()
}