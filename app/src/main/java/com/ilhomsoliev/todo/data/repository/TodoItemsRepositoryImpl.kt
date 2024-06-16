package com.ilhomsoliev.todo.data.repository

import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.models.TodoPriority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    override fun getTodos(): StateFlow<List<TodoItemModel>> = _todos

    override fun getTodoById(todoId: String): TodoItemModel? {
        val id = _todos.value.indexOfFirst { it.id == todoId }
        if (id == -1) return null
        return _todos.value[id]
    }

    override fun addTodo(todo: TodoItemModel): Boolean {
        _todos.value = _todos.value.toMutableList().apply { add(todo) }
        return true
    }

    override fun updateTodo(todo: TodoItemModel): Boolean {
        val id = _todos.value.indexOfFirst { it.id == todo.id }
        if (id == -1) return false
        _todos.value = _todos.value.toMutableList().apply { set(id, todo) }
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