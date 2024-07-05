package com.ilhomsoliev.todo.data.source.remote.models.request

import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodoResponse

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

fun TodoRequest.map() = TodoResponse(
    changed_at = changed_at,
    color = color,
    created_at = created_at,
    deadline = deadline,
    done = done,
    id = id,
    importance = importance,
    last_updated_by = last_updated_by,
    text = text,
)