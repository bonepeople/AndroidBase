package com.bonepeople.android.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.bonepeople.android.base.databinding.ViewTitleBinding
import com.bonepeople.android.widget.util.AppView.show
import com.bonepeople.android.widget.util.AppView.singleClick

class TitleView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    val views: ViewTitleBinding = ViewTitleBinding.inflate(LayoutInflater.from(context), this, true)
    var title: CharSequence = ""
        set(value) {
            views.textViewTitleName.text = value
            field = value
        }

    fun onBackClick(action: (ImageView) -> Unit) {
        views.imageViewTitleBack.singleClick(action = action)
        views.imageViewTitleBack.show()
    }

    fun onActionClick(action: (ImageView) -> Unit) {
        views.imageViewTitleAction.singleClick(action = action)
        views.imageViewTitleAction.show()
    }
}