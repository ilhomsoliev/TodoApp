package com.ilhomsoliev.todo.data.source.remote

import com.ilhomsoliev.todo.core.NetworkConstants
import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.core.toResultState
import com.ilhomsoliev.todo.data.source.remote.models.request.AddTodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.request.EditTodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.request.TodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.response.delete.DeleteTodoResponse
import com.ilhomsoliev.todo.data.source.remote.models.response.edit.EditTodoResponse
import com.ilhomsoliev.todo.data.source.remote.models.response.getById.GetTodoResponse
import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodosResponse
import io.ktor.client.request.headers
import io.ktor.client.request.setBody

class TodoNetworkManager(
    private val webSource: KtorSource
) {
    suspend fun getTodos(): ResultState<TodosResponse> =
        webSource.tryGetResult("${NetworkConstants.PREFIX}/list")
            .let { return it.toResultState() }

    suspend fun addTodo(revision: Int, todo: TodoRequest): ResultState<EditTodoResponse> =
        webSource.tryPostResult("${NetworkConstants.PREFIX}/list") {
            headers {
                append(NetworkConstants.REVISION_HEADER, revision.toString())
            }
            setBody(AddTodoRequest(element = todo))
        }
            .let { return it.toResultState() }

    suspend fun editTodo(revision: Int, todo: TodoRequest): ResultState<GetTodoResponse> =
        webSource.tryPutResult("${NetworkConstants.PREFIX}/list/${todo.id}") {
            headers {
                append(NetworkConstants.REVISION_HEADER, revision.toString())
            }
            setBody(EditTodoRequest(element = todo))
        }.let { return it.toResultState() }

    suspend fun deleteTodo(revision: Int, todoId: String): ResultState<DeleteTodoResponse> =
        webSource.tryDeleteResult("${NetworkConstants.PREFIX}/list/$todoId") {
            headers {
                append(NetworkConstants.REVISION_HEADER, revision.toString())
            }
        }.let { return it.toResultState() }

    suspend fun getTodoById(revision: Int, todoId: String): ResultState<GetTodoResponse> =
        webSource.tryGetResult("${NetworkConstants.PREFIX}/list/$todoId") {
            headers {
                append(NetworkConstants.REVISION_HEADER, revision.toString())
            }
        }.let { return it.toResultState() }

    suspend fun updateList(revision: Int, todos: List<TodoRequest>): ResultState<TodosResponse> =
        webSource.tryPatchResult("${NetworkConstants.PREFIX}/list") {
            headers {
                append(NetworkConstants.REVISION_HEADER, revision.toString())
            }
            setBody(todos)
        }.let { return it.toResultState() }
}