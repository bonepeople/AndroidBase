package com.bonepeople.android.base.viewbinding

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.databinding.DialogContainerBinding
import com.bonepeople.android.widget.util.AppView.gone
import java.lang.reflect.ParameterizedType

/**
 * A general-purpose DialogFragment.
 * + Encapsulates the display and dismissal logic based on DialogFragment behavior to match common usage patterns and avoid potential issues.
 * + Supports ViewBinding for easier implementation of simple dialogs.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class ViewBindingDialogFragment2<V : ViewBinding> : DialogFragment() {
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
     * Sets the FragmentManager and tag used to display the dialog.
     */
    fun setManagerAndTag(manager: FragmentManager, tag: String) {
        dialogFragmentManager = manager
        dialogFragmentTag = tag
    }

    /**
     * Displays the dialog.
     * + If the dialog is already shown, calling this method again has no effect.
     * + Must call [setManagerAndTag] before displaying.
     */
    @CallSuper
    open fun show() {
        require(dialogFragmentManager != null) { "FragmentManager and tag must be set using setManagerAndTag before calling show." }
        show(dialogFragmentManager!!, dialogFragmentTag)
    }

    /**
     * Initializes the view.
     * + Called in onViewCreated.
     */
    protected abstract fun initView()
}