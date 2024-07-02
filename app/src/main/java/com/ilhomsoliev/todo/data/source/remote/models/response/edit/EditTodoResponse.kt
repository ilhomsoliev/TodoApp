package com.ilhomsoliev.todo.data.source.remote.models.response.edit

import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodoResponse

data class EditTodoResponse(
    val element: TodoResponse,
    val revision: Int,
    val status: String
)