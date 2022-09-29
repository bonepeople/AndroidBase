package com.bonepeople.android.base.example.adapter

import com.bonepeople.android.base.ViewBindingRecyclerAdapter
import com.bonepeople.android.base.databinding.ItemSimpleListBinding
import com.bonepeople.android.widget.util.singleClick

class SimpleViewBindingRecyclerAdapter(override val list: List<SimpleData>) : ViewBindingRecyclerAdapter<ItemSimpleListBinding, SimpleData>() {
    override fun updateView(views: ItemSimpleListBinding, data: SimpleData, position: Int) {
        views.textViewContent.text = data.name
        //...
        views.root.singleClick {
            //...
        }
    }
}