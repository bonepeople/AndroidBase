package com.bonepeople.android.base.example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonepeople.android.base.databinding.ItemSimpleListBinding
import com.bonepeople.android.widget.util.singleClick

class SimpleRecyclerViewAdapter(private val list: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataHolder(ItemSimpleListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DataHolder -> {
                holder.binding.let { views ->
                    val data = list[position]
                    //...
                    views.root.singleClick {
                        //...
                    }
                }
            }
        }
    }

    override fun getItemCount() = list.size

    private class DataHolder(val binding: ItemSimpleListBinding) : RecyclerView.ViewHolder(binding.root)
}