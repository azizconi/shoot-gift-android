package com.example.myapplication.utils

import java.text.SimpleDateFormat

fun convertDate(date: String): String {
    val formatDate = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
    return simpleDateFormat.format(formatDate.parse(date))
}