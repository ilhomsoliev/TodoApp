package com.ilhomsoliev.todo.data.source.remote.models.response.getById

import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodoResponse

data class GetTodoResponse(
    val element: TodoResponse,
    val revision: Int,
    val status: String
)