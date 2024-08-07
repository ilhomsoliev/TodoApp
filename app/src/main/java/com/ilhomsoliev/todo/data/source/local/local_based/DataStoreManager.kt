package com.ilhomsoliev.todo.data.source.local.local_based

import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject

class DataStoreManager @Inject constructor() {
    private var revision = 0
    fun changeRevision(value: Int) {
        revision = value
    }

    fun getRevision(): Int = revision

    companion object {
        val hasError = mutableStateOf(false)
    }
}