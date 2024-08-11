package com.bonepeople.android.base.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonepeople.android.base.sample.data.ProductInfo
import com.bonepeople.android.base.util.CoroutineExtension.launchOnDefault
import com.bonepeople.android.widget.util.AppRandom
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class ProductListViewModel : ViewModel() {
    val showLoading = MutableStateFlow(false)
    val countText = MutableStateFlow("")
    val listData = MutableStateFlow(emptyList<ProductInfo>())

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launchOnDefault {
            showLoading.value = true
            delay(2000)
            val count = AppRandom.randomInt(1..30)
            val list = (1..count).map {
                ProductInfo().apply { name = AppRandom.randomString(5) }
            }
            countText.value = "count:$count"
            listData.value = list
            showLoading.value = false
        }
    }
}