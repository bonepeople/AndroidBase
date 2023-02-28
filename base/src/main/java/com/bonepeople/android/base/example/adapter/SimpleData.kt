package com.bonepeople.android.base.example.adapter

import androidx.recyclerview.widget.DiffUtil

class SimpleData {
    var id = 1
    var name = ""
    var value = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleData

        if (id != other.id) return false
        if (name != other.name) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    object Comparator : DiffUtil.ItemCallback<SimpleData>() {
        override fun areItemsTheSame(oldItem: SimpleData, newItem: SimpleData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SimpleData, newItem: SimpleData): Boolean {
            return oldItem == newItem
        }
    }
}