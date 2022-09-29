package com.bonepeople.android.base.example.adapter

import androidx.recyclerview.widget.DiffUtil

object SimpleDataComparator : DiffUtil.ItemCallback<SimpleData>() {

    /**
     * 判断两个数据是否是同一条数据，用于确定新增和移除的数据
     */
    override fun areItemsTheSame(oldItem: SimpleData, newItem: SimpleData): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * 判断两个数据内容是否一致，用于确定数据是否做过更新
     */
    override fun areContentsTheSame(oldItem: SimpleData, newItem: SimpleData): Boolean {
        return oldItem == newItem
    }
}