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

    /**
     * Apply configuration settings
     */
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

    /**
     * Configuration holder
     * + Can be used to save and apply unified styles
     */
    class Config {
        /**
         * Background of the entire title view
         */
        var titleViewBackground: Drawable? = null

        /**
         * Background for the status bar area
         */
        var statusBarBackground: Drawable? = null

        /**
         * Background for the title bar
         */
        var titleBarBackground: Drawable? = null

        /**
         * Text and icon color in the title bar
         */
        @ColorInt
        var titleColor: Int? = null

        /**
         * Height of the title bar
         * + Also affects the button sizes
         */
        var titleBarHeight: Float? = null

        /**
         * Default text shown in the title bar
         */
        var defaultTitleText: String? = null
    }
}