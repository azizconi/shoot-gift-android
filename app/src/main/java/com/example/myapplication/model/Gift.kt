package com.example.myapplication.model

import com.example.myapplication.utils.Constants


data class Gift(
    val inside: String,
    val isWinGift: Boolean,
    val logo: Int?
)

fun levelsGame(level: Int): List<Gift>? {
    return when (level) {
        1 -> {
            listOf(
                Gift("300 мб интернет", true, Constants.INTERNET_ICON),
                Gift("3 дня безлимитного интернета", true, Constants.INTERNET_ICON),
                Gift("300 мин внутри сети", true, Constants.CALL_ICON),
                Gift("Empty", false, null),
                Gift("Empty", false, null),
                Gift("Empty", false, null),
            )

        }
        2 -> {
            listOf(
                Gift("3000 мб интернет", true, Constants.INTERNET_ICON),
                Gift("5 месяца безлимитного интернета", true, Constants.INTERNET_ICON),
                Gift("300 мин внутри сети, 10000 мб", true, Constants.CALL_ICON),
                Gift("Empty", false, null),
                Gift("Empty", false, null),
                Gift("Empty", false, null),
            )

        }
        3 -> {
            listOf(
                Gift("1 год безлимитного интернета", true, Constants.INTERNET_ICON),
            )

        }
        else -> {
            null
        }
    }
}

