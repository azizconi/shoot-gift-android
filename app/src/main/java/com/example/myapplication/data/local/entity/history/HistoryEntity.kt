package com.example.myapplication.data.local.entity.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_game")
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val prizeWon: String,
    val level: Int
)