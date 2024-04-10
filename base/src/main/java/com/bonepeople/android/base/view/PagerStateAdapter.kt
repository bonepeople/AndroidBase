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

@Suppress("UNUSED")
class PagerStateAdapter(var loadMessage: String = "Loading...", var noMoreMessage: String? = null, var errorMessage: String? = null) : LoadStateAdapter<RecyclerView.ViewHolder>() {
    private var customView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RecyclerView.ViewHolder {
        return StateViewHolder(ItemPagerStateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        (holder as StateViewHolder).binding.let { views ->
            if (customView != null) {
                (customView?.parent as? FrameLayout)?.removeAllViews()
                views.frameLayoutContainer.removeAllViews()
                views.frameLayoutContainer.addView(customView)
                views.frameLayoutContainer.show()
                views.textViewStatus.gone()
            } else {
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

    fun setView(view: View? = null) = apply {
        customView = view
    }

    private class StateViewHolder(val binding: ItemPagerStateBinding) : RecyclerView.ViewHolder(binding.root)
}