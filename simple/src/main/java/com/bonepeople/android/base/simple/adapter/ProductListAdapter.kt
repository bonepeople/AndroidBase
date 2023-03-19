package com.bonepeople.android.base.simple.adapter

import com.bonepeople.android.base.viewbinding.ViewBindingRecyclerAdapter
import com.bonepeople.android.base.simple.data.ProductInfo
import com.bonepeople.android.base.simple.databinding.ItemProductListBinding

class ProductListAdapter(override val list: List<ProductInfo>) : ViewBindingRecyclerAdapter<ItemProductListBinding, ProductInfo>() {
    override fun onBindView(views: ItemProductListBinding, data: ProductInfo, position: Int, payloads: MutableList<Any>) {
        views.textViewId.text = "position:$position"
        views.textViewName.text = data.name
    }
}