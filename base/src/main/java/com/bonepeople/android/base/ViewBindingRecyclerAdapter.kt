package com.bonepeople.android.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * RecyclerView.Adapter抽象类
 * + 包含自动实例化的ViewBinding和一个通用的[updateView]函数
 * + 传入的两个泛型分别对应布局的ViewBinding和数据的类型
 */
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

    /**
     * 更新视图
     * @param views 视图所对应的ViewBinding类实例
     * @param data 当前项目对应的数据对象
     * @param position 当前视图的位置
     */
    protected abstract fun updateView(views: V, data: D, position: Int)

    private class DataHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
}