package com.ilhomsoliev.todo.core

import com.ilhomsoliev.todo.BuildConfig


object NetworkConstants {
    const val HOST = "hive.mrdekk.ru"
    const val BASE_URL = " https://hive.mrdekk.ru"
    const val PREFIX = "$BASE_URL/todo"
    const val TOKEN = BuildConfig.API_KEY
    const val OAUTH = "OAuth " + BuildConfig.YAPASSPORT

    const val REVISION_HEADER = "X-Last-Known-Revision"
}