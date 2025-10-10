package com.bonepeople.android.base.sample.module.test.recycleradapter

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonepeople.android.base.sample.databinding.FragmentProductListBinding
import com.bonepeople.android.base.viewbinding.ViewBindingFragment
import com.bonepeople.android.base.util.FlowExtension.observeWithLifecycle
import com.bonepeople.android.widget.util.AppView.singleClick
import com.bonepeople.android.widget.view.LinearItemDecoration

class ProductListFragment : ViewBindingFragment<FragmentProductListBinding>() {
    private val viewModel: ProductListViewModel by viewModels()

    override fun initView() {
        views.titleView.title = "List Adapter"
        viewModel.showLoading.observeWithLifecycle(viewLifecycleOwner) {
            simpleLoadingDialog.switchShow(it)
        }
        views.buttonRefresh.singleClick { viewModel.updateData() }
        viewModel.countText.observeWithLifecycle(viewLifecycleOwner) { views.textViewCount.text = it }
        views.recyclerView.layoutManager = LinearLayoutManager(activity)
        views.recyclerView.addItemDecoration(LinearItemDecoration(12f))
        views.recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        viewModel.listData.observeWithLifecycle(viewLifecycleOwner) {
            views.recyclerView.adapter = ProductListAdapter(it)
        }
    }
}