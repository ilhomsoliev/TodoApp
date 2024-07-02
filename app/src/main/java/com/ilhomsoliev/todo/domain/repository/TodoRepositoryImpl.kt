package com.ilhomsoliev.todo.domain.repository

import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.core.map
import com.ilhomsoliev.todo.data.source.local.dao.TodoDao
import com.ilhomsoliev.todo.data.source.local.local_based.DataStoreManager
import com.ilhomsoliev.todo.data.source.remote.TodoManager
import com.ilhomsoliev.todo.domain.models.TodoModel
import com.ilhomsoliev.todo.domain.models.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class TodoRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
    private val todoManager: TodoManager,
    private val todoDao: TodoDao,
) : TodoRepository {
    private val _showCompleted = MutableStateFlow(false)

    override suspend fun setShowCompleted(showCompleted: Boolean): ResultState<Unit> {
        _showCompleted.emit(showCompleted)
        return ResultState.Success(Unit)
    }

    override fun getShowCompleted(): Flow<Boolean> {
        return _showCompleted.asStateFlow()
    }

    override suspend fun getTodos(): ResultState<List<TodoModel>> {
        return todoManager.getTodos().map {
            dataStoreManager.changeRevision(it.revision)
            val todos = it.list.map { it.map() }
            todoDao.upsertAll(todos.map { it.mapToEntity() })
            todos
        }
    }

    override fun observeTodos(): Flow<ResultState<List<TodoModel>>> {
        return combine(todoDao.getTodos(), _showCompleted) { todos, showCompleted ->
            val newTodos = todos.map { it.map() }
            if (showCompleted) {
                ResultState.Success(newTodos.filter { !it.done })
            } else {
                ResultState.Success(newTodos)
            }
        }
//        return todoDao.getTodos().map {
//            ResultState.Success(it.map { it.map() })
//        }
    }

    override suspend fun addTodo(request: TodoModel): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        todoDao.insert(request.mapToEntity())
        return todoManager.addTodo(revision, request.mapToRequest()).map {
            dataStoreManager.changeRevision(it.revision)
            it.element.map()
        }
    }

    override suspend fun editTodo(request: TodoModel): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        todoDao.insert(request.mapToEntity())
        return todoManager.editTodo(revision, request.mapToRequest()).map {
            dataStoreManager.changeRevision(it.revision)
//            todoDao.insert(it.element.map().mapToEntity())
            it.element.map()
        }
    }

    override suspend fun deleteTodo(id: String): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        todoDao.deleteById(id)
        return todoManager.deleteTodo(revision, id).map {
            dataStoreManager.changeRevision(it.revision)
            it.element.map()
        }
    }

    override suspend fun getTodoById(id: String): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        val todo = todoDao.getTodoById(id)
        if (todo != null) return ResultState.Success(todo.map())
        return todoManager.getTodoById(revision, id).map {
            dataStoreManager.changeRevision(it.revision)
            todoDao.insert(it.element.map().mapToEntity())
            it.element.map()
        }
    }

    override suspend fun updateList(): ResultState<List<TodoModel>> {
        val revision = dataStoreManager.getRevision()
        val todos = todoDao.getTodos().first().map { it.map() }
        return todoManager.updateList(revision, todos.map { it.mapToRequest() }).map {
            dataStoreManager.changeRevision(it.revision)
            val todosResponse = it.list.map { it.map() }
            todoDao.upsertAll(todosResponse.map { it.mapToEntity() })
            todosResponse
        }
    }
}