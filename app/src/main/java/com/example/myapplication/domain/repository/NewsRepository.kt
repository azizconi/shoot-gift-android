package com.example.myapplication.domain.repository

import NewsResponse
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<Resource<NewsResponse>>
}