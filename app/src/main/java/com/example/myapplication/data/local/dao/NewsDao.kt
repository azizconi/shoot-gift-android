package com.example.myapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.entity.news.ArticleEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(articleEntities: List<ArticleEntity>)

    @Query("select * from news")
    fun getNews(): LiveData<List<ArticleEntity>>

}