package com.ilhomsoliev.todo.data.source.remote.models.response.delete

import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodoResponse

data class DeleteTodoResponse(
    val element: TodoResponse,
    val revision: Int,
    val status: String
)