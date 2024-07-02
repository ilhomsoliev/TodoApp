package com.ilhomsoliev.todo.data.source.remote.models.request

data class AddTodoRequest(
    val element: TodoRequest,
    val status: String = "ok"
)