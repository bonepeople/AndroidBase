package com.bonepeople.android.base.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.shade.Protector
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class ViewBindingRecyclerAdapter<V : ViewBinding, D> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected abstract val list: List<D>

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
        val data = list[position]
        onBindView(views, data, position, payloads)
    }

    override fun getItemCount() = list.size

    protected open fun onCreateView(views: V) {}

    protected abstract fun onBindView(views: V, data: D, position: Int, payloads: List<Any>)

    private class DataHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
}