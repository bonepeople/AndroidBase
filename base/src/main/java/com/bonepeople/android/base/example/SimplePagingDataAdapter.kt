package com.bonepeople.android.base.example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bonepeople.android.base.databinding.ItemSimpleListBinding

class SimplePagingDataAdapter : PagingDataAdapter<SimpleData, RecyclerView.ViewHolder>(SimpleDataComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataHolder(ItemSimpleListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DataHolder -> {
                holder.binding.let { views ->
                    getItem(position)?.let { data ->
                        //...
                        views.root.setOnClickListener {
                            //...
                        }
                    }
                }
            }
        }
    }

    private class DataHolder(val binding: ItemSimpleListBinding) : RecyclerView.ViewHolder(binding.root)
}