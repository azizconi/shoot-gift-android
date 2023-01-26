package com.example.myapplication.data.repository

import NewsResponse
import com.example.myapplication.data.remote.Api
import com.example.myapplication.domain.repository.NewsRepository
import com.example.myapplication.utils.Resource
import com.example.myapplication.utils.safeApiCall
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val api: Api
): NewsRepository {
    override fun getNews(): Flow<Resource<NewsResponse>> = safeApiCall {
        api.getNews()
    }
}