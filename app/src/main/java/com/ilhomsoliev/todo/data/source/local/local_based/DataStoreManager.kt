package com.ilhomsoliev.todo.data.source.local.local_based

class DataStoreManager {
    private var revision = 0
    fun changeRevision(value: Int) {
        revision = value
    }

    fun getRevision(): Int = revision
}