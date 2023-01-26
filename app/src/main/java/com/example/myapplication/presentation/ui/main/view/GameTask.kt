package com.example.myapplication.ui.view

import com.example.myapplication.data.model.gift.Gift
import com.example.myapplication.data.model.local.position.Position


interface GameTask {
    fun nextLevel(gift: Gift, position: Position)
    fun loseGame(message: String)
}