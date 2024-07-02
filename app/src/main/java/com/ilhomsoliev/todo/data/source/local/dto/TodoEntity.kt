package com.ilhomsoliev.todo.data.source.local.dto

import androidx.room.Entity

@Entity("todo_table")
data class TodoEntity(
    val changedAt: Long,
    val color: String,
    val createdAt: Long,
    val deadline: Long,
    val done: Boolean,
    val id: String,
    val importance: String,
    val lastUpdatedBy: String,
    val text: String
)