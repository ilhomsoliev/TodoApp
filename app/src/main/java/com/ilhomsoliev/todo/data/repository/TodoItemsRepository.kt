package com.ilhomsoliev.todo.data.repository

import com.ilhomsoliev.todo.data.models.TodoItemModel
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    fun getTodos(): Flow<List<TodoItemModel>>
    fun getDoneTodosAmount(): Flow<Int>
    suspend fun getTodoById(todoId: String): TodoItemModel?
    suspend fun insertTodo(todo: TodoItemModel): Boolean
    suspend fun deleteTodo(todoId: String): Boolean
    suspend fun setShowCompleted(showCompleted: Boolean)
    fun getShowCompleted(): Flow<Boolean>
    suspend fun markTodoAsValue(todoId: String, value: Boolean? = null): Boolean // switches value if null

}