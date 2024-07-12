package com.ilhomsoliev.todo.domain.repository

import com.ilhomsoliev.todo.core.ErrorTypes
import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.core.map
import com.ilhomsoliev.todo.core.on
import com.ilhomsoliev.todo.data.source.local.dao.TodoLocalDao
import com.ilhomsoliev.todo.data.source.local.local_based.DataStoreManager
import com.ilhomsoliev.todo.data.source.remote.TodoNetworkManager
import com.ilhomsoliev.todo.domain.models.TodoModel
import com.ilhomsoliev.todo.domain.models.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val todoNetworkManager: TodoNetworkManager,
    private val todoLocalDao: TodoLocalDao,
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
        return try {
            todoNetworkManager.getTodos().map {
                dataStoreManager.changeRevision(it.revision)
                val todos = it.list.map { it.map() }
                todoLocalDao.upsertAll(todos.map { it.mapToEntity() })
                todos
            }
        } catch (e: Exception) {
            ResultState.Error(types = listOf(ErrorTypes.Network))
        }
    }

    override fun getDoneTodosAmount(): Flow<Int> = todoLocalDao.getTodos().map {
        it.count { item -> item.done }
    }


    override fun observeTodos(): Flow<ResultState<List<TodoModel>>> {
        return combine(todoLocalDao.getTodos(), _showCompleted) { todos, showCompleted ->
            val newTodos = todos.map { it.map() }
            if (showCompleted) {
                ResultState.Success(newTodos.filter { !it.done })
            } else {
                ResultState.Success(newTodos)
            }
        }
    }

    override suspend fun addTodo(request: TodoModel): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        if (request.text.isEmpty()) return ResultState.Error(types = listOf(ErrorTypes.Unspecified))
        todoLocalDao.insert(request.mapToEntity())
        return try {
            todoNetworkManager.addTodo(revision, request.mapToRequest()).map {
                dataStoreManager.changeRevision(it.revision)
                it.element.map()
            }
        } catch (e: Exception) {
            ResultState.Error(types = listOf(ErrorTypes.Network))
        }
    }

    override suspend fun editTodo(request: TodoModel): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        todoLocalDao.insert(request.mapToEntity())
        return try {
            todoNetworkManager.editTodo(revision, request.mapToRequest()).map {
                dataStoreManager.changeRevision(it.revision)
//            todoDao.insert(it.element.map().mapToEntity())
                it.element.map()
            }
        } catch (e: Exception) {
            ResultState.Error(types = listOf(ErrorTypes.Network))
        }

    }

    override suspend fun deleteTodo(id: String): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        todoLocalDao.deleteById(id)
        return try {
            todoNetworkManager.deleteTodo(revision, id).map {
                dataStoreManager.changeRevision(it.revision)
                it.element.map()
            }
        } catch (e: Exception) {
            ResultState.Error(types = listOf(ErrorTypes.Network))
        }
    }

    override suspend fun getTodoById(id: String): ResultState<TodoModel> {
        val revision = dataStoreManager.getRevision()
        val todo = todoLocalDao.getTodoById(id)
        if (todo != null) return ResultState.Success(todo.map())
        return try {
            todoNetworkManager.getTodoById(revision, id).map {
                dataStoreManager.changeRevision(it.revision)
                todoLocalDao.insert(it.element.map().mapToEntity())
                it.element.map()
            }
        } catch (e: Exception) {
            ResultState.Error(types = listOf(ErrorTypes.Network))
        }
    }

    override suspend fun updateList(): ResultState<List<TodoModel>> {
        val revision = dataStoreManager.getRevision()
        val todos = todoLocalDao.getTodos().first().map { it.map() }
        return try {
            todoNetworkManager.updateList(revision, todos.map { it.mapToRequest() }).map {
                dataStoreManager.changeRevision(it.revision)
                val todosResponse = it.list.map { it.map() }
                DataStoreManager.hasError.value = false
                todoLocalDao.upsertAll(todosResponse.map { it.mapToEntity() })
                todosResponse
            }
        } catch (e: Exception) {
            ResultState.Error(types = listOf(ErrorTypes.Network))
        }
    }

    override suspend fun markTodoAsValue(todoId: String): ResultState<TodoModel> {
        val todo = getTodoById(todoId)
        return todo.on(success = {
            editTodo(it.copy(done = !it.done))
        })
    }
}