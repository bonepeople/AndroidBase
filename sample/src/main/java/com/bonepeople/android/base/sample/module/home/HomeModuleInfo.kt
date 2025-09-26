package com.bonepeople.android.base.sample.module.home

data class HomeModuleInfo(
    val title: String,
    val description: String,
    val onClick: () -> Unit
)