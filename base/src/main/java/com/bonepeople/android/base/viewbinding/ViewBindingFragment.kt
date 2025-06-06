package com.bonepeople.android.base.viewbinding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.base.CoroutineLifecycleObserver
import com.bonepeople.android.base.view.CustomLoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Abstract Fragment class.
 * + Includes automatic instantiation of ViewBinding and a basic LoadingDialog.
 * + The generic parameter requires the ViewBinding of the current screen. This ViewBinding will be instantiated and loaded during initialization,
 *   and is available via the `views` property for use in subclasses.
 * + The LoadingDialog is lazily loaded and does not consume resources if unused.
 */
abstract class ViewBindingFragment<V : ViewBinding> : Fragment(), CoroutineScope {
    @Deprecated("CoroutineScope will no longer be supported. This method will be removed from version 1.7.0.")
    override val coroutineContext: CoroutineContext by lazy {
        (Dispatchers.Main + Job()).also {
            viewLifecycleOwner.lifecycle.addObserver(CoroutineLifecycleObserver(it))
        }
    }

    @Deprecated(
        message = "Use viewLifecycleOwner.lifecycleScope.launch(context, start, block) instead. This method will be removed from version 1.7.0.",
        replaceWith = ReplaceWith("viewLifecycleOwner.lifecycleScope.launch(context, start, block)", "androidx.lifecycle.lifecycleScope", "kotlinx.coroutines.launch")
    )
    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = viewLifecycleOwner.lifecycleScope.launch(context, start, block)

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