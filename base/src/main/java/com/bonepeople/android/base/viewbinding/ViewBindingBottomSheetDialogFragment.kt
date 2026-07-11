package com.bonepeople.android.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class ViewBindingBottomSheetDialogFragment<V : ViewBinding> : BottomSheetDialogFragment() {
    private var dialogFragmentManager: FragmentManager? = null
    private var dialogFragmentTag: String = ""

    @Suppress("UNCHECKED_CAST")
    protected val views by lazy {
        Protector.protect {
            val superclass = javaClass.genericSuperclass as ParameterizedType
            val subclass = superclass.actualTypeArguments[0] as Class<*>
            val method = subclass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            method.invoke(null, layoutInflater) as V
        }
    }

    override fun getTheme(): Int = R.style.AndroidBase_NoBackgroundBottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, savedInstanceState: Bundle?): View {
        return views.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    override fun onDestroyView() {
        (views.root.parent as? ViewGroup)?.removeView(views.root)
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
    open fun show(lifecycleOwner: LifecycleOwner? = null) {
        require(dialogFragmentManager != null) { "FragmentManager and tag must be set using setManagerAndTag before calling show." }
        if (isAdded) return
        val owner = lifecycleOwner
        if (owner == null) {
            kotlin.runCatching {
                if (!dialogFragmentManager!!.isStateSaved) {
                    show(dialogFragmentManager!!, dialogFragmentTag)
                }
            }
            return
        }
        owner.lifecycleScope.launch {
            owner.withResumed {}
            delay(177)
            owner.withResumed {}
            show(dialogFragmentManager!!, dialogFragmentTag)
        }
    }

    /**
     * Dismisses the dialog.
     * + If the dialog is not in the foreground, it will wait until the UI is resumed before dismissing.
     */
    @CallSuper
    override fun dismiss() {
        if (lifecycle.currentState == Lifecycle.State.INITIALIZED) return
        lifecycleScope.launch {
            withResumed {}
            delay(177)
            withResumed {}
            super.dismiss()
        }
    }

    /**
     * Initializes the view.
     * + Called in onViewCreated.
     */
    protected abstract fun initView()
}