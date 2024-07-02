package com.ilhomsoliev.todo.data.source

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.plugins.logging.Logger

val logsState = mutableStateOf<String?>(null)

object LogAdapter : Logger {
    override fun log(message: String) {
        logsState.value = message + "\n"
        Log.d("KTOR", message)
    }
}