package com.bonepeople.android.base.simple.adapter

import com.bonepeople.android.base.ViewBindingRecyclerAdapter
import com.bonepeople.android.base.simple.data.ProductInfo
import com.bonepeople.android.base.simple.databinding.ItemProductListBinding

class ProductListAdapter(override val list: List<ProductInfo>) : ViewBindingRecyclerAdapter<ItemProductListBinding, ProductInfo>() {
    override fun updateView(views: ItemProductListBinding, data: ProductInfo, position: Int) {
        views.textViewId.text = "position:$position"
        views.textViewName.text = data.name
    }
}