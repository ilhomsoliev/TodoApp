package com.ilhomsoliev.todo.data.source.remote.models.request

data class TodoRequest(
    val changed_at: Long,
    val color: String,
    val created_at: Long,
    val deadline: Long,
    val done: Boolean,
    val id: String,
    val importance: String,
    val last_updated_by: String,
    val text: String
)