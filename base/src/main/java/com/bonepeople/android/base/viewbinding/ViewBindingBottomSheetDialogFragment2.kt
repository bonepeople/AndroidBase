package com.bonepeople.android.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.reflect.ParameterizedType

@Suppress("MemberVisibilityCanBePrivate")
abstract class ViewBindingBottomSheetDialogFragment2<V : ViewBinding> : BottomSheetDialogFragment() {
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