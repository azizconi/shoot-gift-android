package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.HistoryDao
import com.example.myapplication.data.local.dao.NewsDao
import com.example.myapplication.data.local.entity.history.HistoryEntity
import com.example.myapplication.data.local.entity.news.ArticleEntity

@Database(entities = [HistoryEntity::class, ArticleEntity::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun newsDao(): NewsDao
}