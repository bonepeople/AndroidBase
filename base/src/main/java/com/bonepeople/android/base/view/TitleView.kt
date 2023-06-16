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
import com.bonepeople.android.dimensionutil.DimensionUtil
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
     * 应用配置信息
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
            titleBarHeight?.let { views.constraintLayoutTitle.layoutParams.height = DimensionUtil.getPx(it) }
            defaultTitleText?.let { views.textViewTitleName.text = it }
        }
    }

    companion object {
        var defaultConfig: Config = Config()
    }

    /**
     * 配置信息
     * + 可用于保存并统一设置样式
     */
    class Config {
        /**
         * 整体背景
         */
        var titleViewBackground: Drawable? = null

        /**
         * 状态栏背景
         */
        var statusBarBackground: Drawable? = null

        /**
         * 标题栏背景
         */
        var titleBarBackground: Drawable? = null

        /**
         * 标题文字和图标的颜色
         */
        @ColorInt
        var titleColor: Int? = null

        /**
         * 标题栏高度
         * + 同样会影响按钮大小
         */
        var titleBarHeight: Float? = null

        /**
         * 标题栏默认文案
         */
        var defaultTitleText: String? = null
    }
}