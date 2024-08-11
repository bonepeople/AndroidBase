package com.bonepeople.android.base.sample.adapter

import com.bonepeople.android.base.viewbinding.ViewBindingRecyclerAdapter
import com.bonepeople.android.base.sample.data.ProductInfo
import com.bonepeople.android.base.sample.databinding.ItemProductListBinding

class ProductListAdapter(override val list: List<ProductInfo>) : ViewBindingRecyclerAdapter<ItemProductListBinding, ProductInfo>() {
    override fun onBindView(views: ItemProductListBinding, data: ProductInfo, position: Int, payloads: List<Any>) {
        views.textViewId.text = "position:$position"
        views.textViewName.text = data.name
    }
}