package com.ilhomsoliev.todo.data.repository

import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.data.models.TodoItemModel
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    fun getTodos(): Flow<ResultState<List<TodoItemModel>>>
    fun getDoneTodosAmount(): Flow<ResultState<Int>>
    suspend fun getTodoById(todoId: String): ResultState<TodoItemModel>
    suspend fun insertTodo(todo: TodoItemModel): ResultState<Unit>
    suspend fun deleteTodo(todoId: String): ResultState<Unit>
    suspend fun setShowCompleted(showCompleted: Boolean): ResultState<Unit>
    fun getShowCompleted(): Flow<Boolean>
    suspend fun markTodoAsValue(
        todoId: String,
        value: Boolean? = null
    ): ResultState<Unit>

}