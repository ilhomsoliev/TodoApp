package com.ilhomsoliev.todo.data.source.remote.models.response.list

data class TodoResponse(
    val changed_at: Int,
    val color: String,
    val created_at: Int,
    val deadline: Int,
    val done: Boolean,
    val id: String,
    val importance: String,
    val last_updated_by: String,
    val text: String
)