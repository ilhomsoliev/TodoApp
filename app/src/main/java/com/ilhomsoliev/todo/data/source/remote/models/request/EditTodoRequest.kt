package com.ilhomsoliev.todo.data.source.remote.models.request

data class EditTodoRequest(
    val element: TodoRequest,
    val status: String = "ok",
)