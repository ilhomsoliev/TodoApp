package com.ilhomsoliev.todo.data.repository

import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.models.TodoPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TodoItemsRepositoryImpl : TodoItemsRepository {
    private val _todos = MutableStateFlow(
        listOf(
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
            TodoItemModel(
                id = "312",
                text = "Todo 3",
                priority = TodoPriority.HIGH,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "3121",
                text = "Todo 3",
                priority = TodoPriority.LOW,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "3123",
                text = "Todo 4",
                priority = TodoPriority.USUAL,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "4",
                text = "Todo 3 asjdfnasjnfjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.HIGH,
                deadline = 1718444071538,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "5",
                text = "Todo 3 asjdfnasjnf asd fas fas dfaasdjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.HIGH,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "55",
                text = "Todo 3 asjdfnasjnf asd fas fas dfaasdjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.HIGH,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "6",
                text = "Todo 3 asjdfnasjnf asd fas fas dfaasdjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.HIGH,
                deadline = null,
                isCompleted = true,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "7",
                text = "Todo 3 asjdfnasjnf asd fas fas dfaasdjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.HIGH,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "8",
                text = "Todo 3 asjdfnasjnf asd fas fas dfaasdjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.LOW,
                deadline = null,
                isCompleted = false,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
            TodoItemModel(
                id = "10",
                text = "Todo 3 asjdfnasjnf asd fas fas dfaasdjkasndfjkansdjfnasjkfnajksdnfjasndfjkansdkjfnjks dnjkaf nsd fnasjkdf njkasd nkjasdn kjasnd f",
                priority = TodoPriority.USUAL,
                deadline = null,
                isCompleted = true,
                createdDate = 1718444071538,
                editedDate = 1718444071538,
            ),
        )
    )

    private val _showCompleted = MutableStateFlow(false)

    override suspend fun setShowCompleted(showCompleted: Boolean): ResultState<Unit> {
        _showCompleted.emit(showCompleted)
        return ResultState.Success(Unit)
    }

    override fun getShowCompleted(): Flow<Boolean> {
        return _showCompleted.asStateFlow()
    }

    override fun getTodos(): Flow<ResultState<List<TodoItemModel>>> =
        combine(_todos, _showCompleted) { todos, showCompleted ->
            if (showCompleted) {
                ResultState.Success(todos.filter { !it.isCompleted })
            } else {
                ResultState.Success(todos)
            }
        }

    override fun getDoneTodosAmount(): Flow<ResultState<Int>> = _todos.map {
        ResultState.Success(it.count { item -> item.isCompleted })
    }

    override suspend fun getTodoById(todoId: String): ResultState<TodoItemModel> {
        val response = _todos.value.firstOrNull { it.id == todoId }
        return if (response == null) ResultState.Error("")
        else ResultState.Success(response)
    }

    override suspend fun insertTodo(todo: TodoItemModel): ResultState<Unit> {
        if (todo.text.isEmpty()) return ResultState.Error()
        val id = _todos.value.indexOfFirst { it.id == todo.id }
        if (id == -1) _todos.emit(_todos.value + listOf(todo))
        else _todos.emit(_todos.value.toMutableList().apply { set(id, todo) })
        return ResultState.Success(Unit)
    }

    override suspend fun deleteTodo(todoId: String): ResultState<Unit> {
        val item = _todos.value.firstOrNull { it.id == todoId }
        if (item == null) return ResultState.Error()
        _todos.emit(_todos.value - item)
        return ResultState.Success(Unit)
    }

    override suspend fun markTodoAsValue(todoId: String, value: Boolean?): ResultState<Unit> {
        val id = _todos.value.indexOfFirst { it.id == todoId }
        if (id == -1) return ResultState.Error()
        val newValue = _todos.value[id].copy(isCompleted = !_todos.value[id].isCompleted)
        _todos.emit(_todos.value.toMutableList().apply { set(id, newValue) })
        return ResultState.Success(Unit)
    }

}

