package com.ilhomsoliev.todo.domain.repository

import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.core.map
import com.ilhomsoliev.todo.data.source.local.local_based.DataStoreManager
import com.ilhomsoliev.todo.data.source.remote.TodoManager
import com.ilhomsoliev.todo.domain.models.TodoModel
import com.ilhomsoliev.todo.domain.models.map

class TodoRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
    private val todoManager: TodoManager
) : TodoRepository {
    override suspend fun getTodos(): ResultState<List<TodoModel>> {
        todoManager.getTodos().map {
            dataStoreManager.changeRevision(it.revision)
            it.revision
            it.list.map { it.map() }
        }
    }

    override suspend fun addTodo(request: TodoModel): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        return todoManager.addTodo(revision, request.mapToRequest()).map {
            dataStoreManager.changeRevision(it.revision)
            it.element.map()
        }
    }

    override suspend fun editTodo(request: TodoModel): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        return todoManager.editTodo(revision, request.mapToRequest()).map {
            dataStoreManager.changeRevision(it.revision)
            it.element.map()
        }
    }

    override suspend fun deleteTodo(id: String): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        return todoManager.deleteTodo(revision, id).map {
            dataStoreManager.changeRevision(it.revision)
            it.element.map()
        }
    }

    override suspend fun getTodoById(id: String): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        return todoManager.getTodoById(revision, id).map {
            dataStoreManager.changeRevision(it.revision)
            it.element.map()
        }
    }

    override suspend fun updateList(todos: List<TodoModel>): ResultState<List<TodoModel>> {
        val revision = dataStoreManager.getRevision()
        return todoManager.updateList(revision, todos.map { it.mapToRequest() }).map {
            dataStoreManager.changeRevision(it.revision)
            it.list.map { it.map() }
        }
    }
}