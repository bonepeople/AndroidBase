package com.bonepeople.android.base.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bonepeople.android.shade.Protector
import java.lang.reflect.ParameterizedType

/**
 * RecyclerView.Adapter抽象类
 * + 包含自动实例化的ViewBinding和一个通用的[onBindView]函数
 * + 传入的两个泛型分别对应布局的ViewBinding和数据的类型
 */
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

    /**
     * 视图首次创建时的回调函数
     * @param views 视图所对应的ViewBinding类实例
     */
    protected open fun onCreateView(views: V) {}

    /**
     * 更新视图
     * @param views 视图所对应的ViewBinding类实例
     * @param data 当前项目对应的数据对象
     * @param position 当前视图的位置
     * @param payloads 局部刷新标志
     */
    protected abstract fun onBindView(views: V, data: D, position: Int, payloads: List<Any>)

    private class DataHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
}