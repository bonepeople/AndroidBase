package com.bonepeople.android.base.simple.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonepeople.android.base.ViewBindingFragment
import com.bonepeople.android.base.simple.adapter.ProductListAdapter
import com.bonepeople.android.base.simple.data.ProductInfo
import com.bonepeople.android.base.simple.databinding.FragmentProductListBinding
import com.bonepeople.android.widget.util.AppRandom
import com.bonepeople.android.widget.util.AppView.singleClick
import com.bonepeople.android.widget.view.LinearItemDecoration
import kotlinx.coroutines.launch

class ProductListFragment : ViewBindingFragment<FragmentProductListBinding>() {
    override fun initView() {
        views.titleView.textViewTitleName.text = "ProductList"
        views.buttonRefresh.singleClick { updateView() }
        views.recyclerView.layoutManager = LinearLayoutManager(activity)
        views.recyclerView.addItemDecoration(LinearItemDecoration(1f).setPadding(20f, 20f).setColor(0xFFCCCCCC.toInt()))
    }

    override fun initData(savedInstanceState: Bundle?) {
        updateView()
    }

    private fun updateView() {
        launch {
            loadingDialog.show()
            val count = AppRandom.randomInt(1..30)
            val list = (1..count).map {
                ProductInfo().apply { name = AppRandom.randomString(5) }
            }
            views.textViewCount.text = "count:$count"
            views.recyclerView.adapter = ProductListAdapter(list)
            loadingDialog.dismiss()
        }
    }
}