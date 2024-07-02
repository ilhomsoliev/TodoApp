package com.ilhomsoliev.todo.domain.repository

import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.domain.models.TodoModel

interface TodoRepository {
    suspend fun getTodos(): ResultState<List<TodoModel>>
    suspend fun addTodo(request: TodoModel): ResultState<TodoModel>
    suspend fun editTodo(request: TodoModel): ResultState<TodoModel>
    suspend fun deleteTodo(id: String): ResultState<TodoModel>
    suspend fun getTodoById(id: String): ResultState<TodoModel>
    suspend fun updateList(todos: List<TodoModel>): ResultState<List<TodoModel>>
}