package com.example.myapplication.data.local.entity.news

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class ArticleEntity (
	@PrimaryKey(autoGenerate = false)
	@SerializedName("title") val title : String,
	@SerializedName("description") val description : String? = null,
	@SerializedName("urlToImage") val urlToImage : String? = null,
)