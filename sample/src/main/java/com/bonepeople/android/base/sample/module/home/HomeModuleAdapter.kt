package com.bonepeople.android.base.sample.module.home

import com.bonepeople.android.base.sample.databinding.ItemHomeModuleBinding
import com.bonepeople.android.base.viewbinding.ViewBindingRecyclerAdapter
import com.bonepeople.android.widget.util.AppView.singleClick

class HomeModuleAdapter(override val list: List<HomeModuleInfo>) : ViewBindingRecyclerAdapter<ItemHomeModuleBinding, HomeModuleInfo>() {
    override fun onBindView(views: ItemHomeModuleBinding, data: HomeModuleInfo, position: Int, payloads: List<Any>) {
        views.textTitle.text = data.title
        views.textDescription.text = data.description
        views.root.singleClick { data.onClick() }
    }
}