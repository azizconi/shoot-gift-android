package com.example.myapplication.data.remote

import NewsResponse
import com.example.myapplication.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("category") category: String = "sport",
        @Query("country") county: String = "ru",
        @Query("apiKey") apiKey: String = Constants.NEWS_API,
    ): Response<NewsResponse>
}