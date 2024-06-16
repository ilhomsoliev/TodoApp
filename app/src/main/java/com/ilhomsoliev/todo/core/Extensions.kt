package com.ilhomsoliev.todo.core

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

private val IS_DEBUG: Boolean = true // TODO BuildConfig.DEBUG

fun generateRandomString(
    length: Int = 20,
    charset: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
): String {
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

fun Any?.printToLog(tag: String = "DEBUG_LOG") {
    if (IS_DEBUG)
        Log.d(tag, toString())
}

fun formatDate(dateInMillis: Long): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
    return dateFormat.format(dateInMillis)
}