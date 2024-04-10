package com.bonepeople.android.base.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.bonepeople.android.base.databinding.ViewTitleBinding
import com.bonepeople.android.widget.util.AppDensity
import com.bonepeople.android.widget.util.AppView.show
import com.bonepeople.android.widget.util.AppView.singleClick

class TitleView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    val views: ViewTitleBinding = ViewTitleBinding.inflate(LayoutInflater.from(context), this, true)
    var title: CharSequence = ""
        set(value) {
            views.textViewTitleName.text = value
            field = value
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        applyConfig(defaultConfig)
    }

    fun onBackClick(action: (ImageView) -> Unit) {
        views.imageViewTitleBack.singleClick(action = action)
        views.imageViewTitleBack.show()
    }

    fun onActionClick(action: (ImageView) -> Unit) {
        views.imageViewTitleAction.singleClick(action = action)
        views.imageViewTitleAction.show()
    }

    fun applyConfig(config: Config) {
        config.run {
            titleViewBackground?.let { views.root.background = it }
            statusBarBackground?.let { views.statusBarHolder.background = it }
            titleBarBackground?.let { views.constraintLayoutTitle.background = it }
            titleColor?.let {
                views.textViewTitleName.setTextColor(it)
                views.imageViewTitleBack.imageTintList = ColorStateList.valueOf(it)
                views.imageViewTitleAction.imageTintList = ColorStateList.valueOf(it)
            }
            titleBarHeight?.let { views.constraintLayoutTitle.layoutParams.height = AppDensity.getPx(it) }
            defaultTitleText?.let { views.textViewTitleName.text = it }
        }
    }

    companion object {
        var defaultConfig: Config = Config()
    }

    class Config {
        var titleViewBackground: Drawable? = null

        var statusBarBackground: Drawable? = null

        var titleBarBackground: Drawable? = null

        @ColorInt
        var titleColor: Int? = null

        var titleBarHeight: Float? = null

        var defaultTitleText: String? = null
    }
}