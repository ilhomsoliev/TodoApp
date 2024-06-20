package com.ilhomsoliev.todo.data.repository

import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.models.TodoPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TodoItemsRepositoryImpl : TodoItemsRepository {
    private val _todos = MutableStateFlow(
        mutableListOf(
            TodoItemModel(
                id = "1",
                text = "Todo 1",
                priority = TodoPriority.USUAL,
                deadline = 1718444071538,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "2",
                text = "Todo 2",
                priority = TodoPriority.LOW,
                deadline = 1718444071538,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "3",
                text = "Todo 3",
                priority = TodoPriority.HIGH,
                deadline = 1718444071538,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
        )
    )
    private val _showCompleted = MutableStateFlow(false)

    override fun setShowCompleted(showCompleted: Boolean) {
        _showCompleted.value = showCompleted
    }

    override fun getTodos(): Flow<List<TodoItemModel>> =
        combine(_todos, _showCompleted) { todos, showCompleted ->
            if (showCompleted) {
                todos.filter { it.isCompleted }
            } else {
                todos
            }
        }

    override fun getDoneTodosAmount(): Flow<Int> = _todos.map {
        it.count { it.isCompleted }
    }

    override fun getTodoById(todoId: String): TodoItemModel? {
        val id = _todos.value.indexOfFirst { it.id == todoId }
        if (id == -1) return null
        return _todos.value[id]
    }

    override fun insertTodo(todo: TodoItemModel): Boolean {
        if (todo.text.isEmpty()) return false
        val id = _todos.value.indexOfFirst { it.id == todo.id }
        if (id == -1) _todos.value = _todos.value.toMutableList().apply { add(todo) }
        else _todos.value = _todos.value.toMutableList().apply { set(id, todo) }
        return true
    }

    override fun deleteTodo(todoId: String): Boolean {
        val id = _todos.value.indexOfFirst { it.id == todoId }
        if (id == -1) return false
        _todos.value = _todos.value.toMutableList().apply { removeAt(id) }
        return true
    }

    override fun markTodoAsValue(todoId: String, value: Boolean?): Boolean {
        val id = _todos.value.indexOfFirst { it.id == todoId }
        if (id == -1) return false
        val newValue = _todos.value[id].copy(isCompleted = !_todos.value[id].isCompleted)
        _todos.value = _todos.value.toMutableList().apply { set(id, newValue) }
        return true
    }

}