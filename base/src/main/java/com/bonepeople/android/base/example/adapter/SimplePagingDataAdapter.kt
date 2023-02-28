package com.bonepeople.android.base.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bonepeople.android.base.databinding.ItemSimpleListBinding
import com.bonepeople.android.widget.util.singleClick

class SimplePagingDataAdapter : PagingDataAdapter<SimpleData, RecyclerView.ViewHolder>(SimpleData.Comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataHolder(ItemSimpleListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DataHolder -> {
                holder.binding.let { views ->
                    getItem(position)?.let { data ->
                        views.textViewContent.text = data.name
                        //...
                        views.root.singleClick {
                            //...
                        }
                    }
                }
            }
        }
    }

    private class DataHolder(val binding: ItemSimpleListBinding) : RecyclerView.ViewHolder(binding.root)
}