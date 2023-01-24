package com.example.myapplication.ui.view

import com.example.myapplication.model.Gift

interface GameTask {
    fun nextLevel(gift: Gift)
    fun loseGame(message: String)
}