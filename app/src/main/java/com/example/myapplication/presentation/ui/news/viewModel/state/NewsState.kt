package com.example.myapplication.presentation.ui.news.viewModel.state

import com.example.myapplication.data.local.entity.news.ArticleEntity

data class NewsState(
    val isLoading: Boolean = true,
    val data: List<ArticleEntity>? = null
)