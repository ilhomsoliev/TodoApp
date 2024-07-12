package com.ilhomsoliev.todo.data.source.remote.models.response.list

data class TodosResponse(
    val list: List<TodoResponse>,
    val revision: Int,
    val status: String
)