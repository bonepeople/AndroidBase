package com.bonepeople.android.base.sample.module.test.recycleradapter

import com.bonepeople.android.base.viewbinding.ViewBindingRecyclerAdapter
import com.bonepeople.android.base.sample.databinding.ItemProductListBinding
import com.bonepeople.android.base.sample.global.data.ProductInfo

class ProductListAdapter(override val list: List<ProductInfo>) : ViewBindingRecyclerAdapter<ItemProductListBinding, ProductInfo>() {
    override fun onBindView(views: ItemProductListBinding, data: ProductInfo, position: Int, payloads: List<Any>) {
        views.textViewIndex.text = "#$position"
        views.textViewName.text = data.name
    }
}