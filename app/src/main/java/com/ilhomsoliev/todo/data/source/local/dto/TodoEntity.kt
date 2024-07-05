package com.ilhomsoliev.todo.data.source.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todo_table")
data class TodoEntity(
    val changedAt: Long,
    val color: String,
    val createdAt: Long,
    val deadline: Long,
    val done: Boolean,
    @PrimaryKey
    val id: String,
    val importance: String,
    val lastUpdatedBy: String,
    val text: String
)