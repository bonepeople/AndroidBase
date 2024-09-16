package com.bonepeople.android.base.example.adapter

import com.bonepeople.android.base.viewbinding.ViewBindingRecyclerAdapter
import com.bonepeople.android.base.databinding.ItemSimpleListBinding
import com.bonepeople.android.widget.util.AppView.singleClick

@Suppress("Unused")
class SimpleViewBindingRecyclerAdapter(override val list: List<SimpleData>) : ViewBindingRecyclerAdapter<ItemSimpleListBinding, SimpleData>() {
    override fun onBindView(views: ItemSimpleListBinding, data: SimpleData, position: Int, payloads: List<Any>) {
        views.textViewContent.text = data.name
        //...
        views.root.singleClick {
            //...
        }
    }
}