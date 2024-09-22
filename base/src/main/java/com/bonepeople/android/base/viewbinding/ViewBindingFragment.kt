package com.bonepeople.android.base.viewbinding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.view.CustomLoadingDialog
import java.lang.reflect.ParameterizedType

/**
 * Abstract Fragment class.
 * + Includes automatic instantiation of ViewBinding and a basic LoadingDialog.
 * + The generic parameter requires the ViewBinding of the current screen. This ViewBinding will be instantiated and loaded during initialization,
 *   and is available via the `views` property for use in subclasses.
 * + The LoadingDialog is lazily loaded and does not consume resources if unused.
 */
abstract class ViewBindingFragment<V : ViewBinding> : Fragment() {
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
     * Initializes the UI.
     * + Called in onViewCreated.
     */
    protected abstract fun initView()

    /**
     * Initializes the data.
     * + Called in onViewCreated.
     */
    protected open fun initData(savedInstanceState: Bundle?) {}
}