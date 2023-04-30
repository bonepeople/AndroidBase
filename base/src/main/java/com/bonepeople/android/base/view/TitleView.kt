package com.bonepeople.android.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bonepeople.android.base.databinding.ViewTitleBinding

class TitleView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    val views = ViewTitleBinding.inflate(LayoutInflater.from(context), this, true)
}