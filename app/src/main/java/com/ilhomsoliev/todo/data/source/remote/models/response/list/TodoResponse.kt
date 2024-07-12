package com.ilhomsoliev.todo.data.source.remote.models.response.list

data class TodoResponse(
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