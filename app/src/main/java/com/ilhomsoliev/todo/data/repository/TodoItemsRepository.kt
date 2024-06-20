package com.ilhomsoliev.todo.data.repository

import com.ilhomsoliev.todo.data.models.TodoItemModel
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    fun getTodos(): Flow<List<TodoItemModel>>
    fun getDoneTodosAmount(): Flow<Int>
    fun getTodoById(todoId: String): TodoItemModel?
    fun addTodo(todo: TodoItemModel): Boolean
    fun updateTodo(todo: TodoItemModel): Boolean
    fun deleteTodo(todoId: String): Boolean
    fun setShowCompleted(showCompleted: Boolean)
    fun markTodoAsValue(todoId: String, value: Boolean? = null): Boolean // switches value if null

}