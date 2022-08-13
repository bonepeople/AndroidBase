package com.bonepeople.android.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class ViewBindingRecyclerAdapter<V : ViewBinding, D> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected abstract val list: List<D>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val subclass = superclass.actualTypeArguments[0] as Class<*>
        val method = subclass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
        val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as V
        return DataHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val views = (holder as DataHolder<V>).binding
        val data = list[position]
        updateView(views, data, position)
    }

    override fun getItemCount() = list.size

    protected abstract fun updateView(views: V, data: D, position: Int)

    private class DataHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
}