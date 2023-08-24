package com.bonepeople.android.base.simple.ui

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonepeople.android.base.simple.adapter.ProductListAdapter
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.simple.databinding.FragmentProductListBinding
import com.bonepeople.android.base.util.FlowExtension.observeWithLifecycle
import com.bonepeople.android.widget.util.AppView.singleClick
import com.bonepeople.android.widget.view.LinearItemDecoration

class ProductListFragment : ViewBindingFragment<FragmentProductListBinding>() {
    private val viewModel: ProductListViewModel by viewModels()

    override fun initView() {
        views.titleView.title = "ProductList"
        viewModel.showLoading.observeWithLifecycle(viewLifecycleOwner) {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        }
        views.buttonRefresh.singleClick { viewModel.updateData() }
        viewModel.countText.observeWithLifecycle(viewLifecycleOwner) { views.textViewCount.text = it }
        views.recyclerView.layoutManager = LinearLayoutManager(activity)
        views.recyclerView.addItemDecoration(LinearItemDecoration(1f).setPadding(20f, 20f).setColor(0xFFCCCCCC.toInt()))
        viewModel.listData.observeWithLifecycle(viewLifecycleOwner) {
            views.recyclerView.adapter = ProductListAdapter(it)
        }
    }
}