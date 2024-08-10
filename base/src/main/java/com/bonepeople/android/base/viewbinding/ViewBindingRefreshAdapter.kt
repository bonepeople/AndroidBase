package com.bonepeople.android.base.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.shade.Protector
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Abstract RecyclerView.Adapter class.
 * + Includes automatic instantiation of ViewBinding and a general-purpose [onBindView] function.
 * + The two generic parameters correspond to the layout's ViewBinding and the data type.
 * + Requires a DiffUtil.ItemCallback for efficient data comparison during automatic refresh.
 * + Use [submitList] to set data and trigger automatic updates.
 */
@Suppress("UNCHECKED_CAST")
abstract class ViewBindingRefreshAdapter<V : ViewBinding, D>(diff: DiffUtil.ItemCallback<D>) : ListAdapter<D, RecyclerView.ViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Protector.protect {
            val superclass = javaClass.genericSuperclass as ParameterizedType
            val subclass = superclass.actualTypeArguments[0] as Class<*>
            val method = subclass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
            val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as V
            onCreateView(binding)
            DataHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, listOf())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        val views = (holder as DataHolder<V>).binding
        val data = getItem(position)
        onBindView(views, data, position, payloads)
    }

    /**
     * Submits the updated list.
     * @param list The new data.
     */
    final override fun submitList(list: List<D>?) {
        super.submitList(ArrayList(list.orEmpty()))
    }

    /**
     * Submits the updated list with a callback.
     * @param list The new data.
     * @param commitCallback Callback to run after the update is committed.
     */
    final override fun submitList(list: List<D>?, commitCallback: Runnable?) {
        super.submitList(ArrayList(list.orEmpty()), commitCallback)
    }

    /**
     * Callback when the view is first created.
     * @param views An instance of the ViewBinding class corresponding to the view.
     */
    protected open fun onCreateView(views: V) {}

    /**
     * Updates the view.
     * @param views An instance of the ViewBinding class corresponding to the view.
     * @param data The data object for the current item.
     * @param position The position of the current view.
     * @param payloads Partial update flags. Should be defined via [DiffUtil.ItemCallback.getChangePayload].
     */
    protected abstract fun onBindView(views: V, data: D, position: Int, payloads: List<Any>)

    private class DataHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
}