package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.myapplication.data.local.dao.HistoryDao
import com.example.myapplication.data.local.dao.NewsDao
import com.example.myapplication.data.local.entity.history.HistoryEntity
import com.example.myapplication.data.local.entity.news.ArticleEntity
import com.example.myapplication.data.local.type_converter.DateConverter

@Database(entities = [HistoryEntity::class, ArticleEntity::class], version = 5)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun newsDao(): NewsDao
}