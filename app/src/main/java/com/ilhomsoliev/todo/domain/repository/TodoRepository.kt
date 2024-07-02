package com.ilhomsoliev.todo.domain.repository

import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.domain.models.TodoModel
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodos(): ResultState<List<TodoModel>>
    suspend fun setShowCompleted(showCompleted: Boolean): ResultState<Unit>
    fun getShowCompleted(): Flow<Boolean>
    fun observeTodos(): Flow<ResultState<List<TodoModel>>>
    suspend fun addTodo(request: TodoModel): ResultState<TodoModel>
    suspend fun editTodo(request: TodoModel): ResultState<TodoModel>
    suspend fun deleteTodo(id: String): ResultState<TodoModel>
    suspend fun getTodoById(id: String): ResultState<TodoModel>
    suspend fun updateList(): ResultState<List<TodoModel>>
}