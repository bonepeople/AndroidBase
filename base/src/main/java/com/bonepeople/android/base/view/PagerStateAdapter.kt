package com.bonepeople.android.base.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bonepeople.android.base.databinding.ItemPagerStateBinding
import com.bonepeople.android.widget.util.AppView.gone
import com.bonepeople.android.widget.util.AppView.show

/**
 * 分页加载的状态指示器
 *
 * 分页加载时在RecyclerView中添加一个item，用于展示加载状态
 * + 通常会配合[androidx.paging.PagingDataAdapter.withLoadStateHeaderAndFooter]使用
 * + 官方的显示逻辑有问题，作为header时不要设置[noMoreMessage]
 */
@Suppress("UNUSED")
class PagerStateAdapter(var loadMessage: String = "Loading...", var noMoreMessage: String? = null, var errorMessage: String? = null) : LoadStateAdapter<RecyclerView.ViewHolder>() {
    private var customView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RecyclerView.ViewHolder {
        return StateViewHolder(ItemPagerStateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        (holder as StateViewHolder).binding.let { views ->
            if (customView != null) {//设置了自定义View
                //出现多个ViewHolder的时候需要通过customView.parent方式移除引用，否则会出现重复加载的异常
                (customView?.parent as? FrameLayout)?.removeAllViews()
                views.frameLayoutContainer.removeAllViews()
                views.frameLayoutContainer.addView(customView)
                views.frameLayoutContainer.show()
                views.textViewStatus.gone()
            } else {//没有设置自定义View
                views.frameLayoutContainer.gone()
                views.textViewStatus.show()
                views.textViewStatus.text = when (loadState) {
                    is LoadState.Error -> errorMessage
                    is LoadState.NotLoading -> noMoreMessage
                    else -> loadMessage
                }
            }
        }
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return when (loadState) {
            is LoadState.Loading -> true
            is LoadState.Error -> errorMessage != null
            is LoadState.NotLoading -> loadState.endOfPaginationReached && noMoreMessage != null
            else -> false
        }
    }

    /**
     * 设置用于展示加载状态的自定义View
     * + 未设置的情况下会使用默认的TextView显示加载状态
     */
    fun setView(view: View? = null) = apply {
        customView = view
    }

    private class StateViewHolder(val binding: ItemPagerStateBinding) : RecyclerView.ViewHolder(binding.root)
}