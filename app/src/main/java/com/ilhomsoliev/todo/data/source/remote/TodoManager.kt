package com.ilhomsoliev.todo.data.source.remote

import com.ilhomsoliev.todo.core.NetworkConstants
import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.data.shared.wrapped
import com.ilhomsoliev.todo.data.source.remote.models.request.AddTodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.request.EditTodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.request.TodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.response.delete.DeleteTodoResponse
import com.ilhomsoliev.todo.data.source.remote.models.response.edit.EditTodoResponse
import com.ilhomsoliev.todo.data.source.remote.models.response.getById.GetTodoResponse
import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodosResponse
import io.ktor.client.request.headers
import io.ktor.client.request.setBody

class TodoManager(
    private val webSource: KtorSource
) {
    suspend fun getTodos() = webSource.tryGetResult("${NetworkConstants.PREFIX}/list") {
        headers {
            append("Authorization", NetworkConstants.TOKEN)
        }
    }.let {
        if (it.isSuccess) {
            val convert = it.getOrNull()?.wrapped<TodosResponse>()
            if (convert != null) {
                ResultState.Success(convert)
            } else {
                ResultState.Error("Null error couldn't convert")
            }
        } else {
            ResultState.Error(it.exceptionOrNull().toString())
        }
    }

    suspend fun addTodo(revision: Int, todo: TodoRequest) =
        webSource.tryPostResult("${NetworkConstants.PREFIX}/list") {
            headers {
                append("Authorization", NetworkConstants.TOKEN)
                append("X-Last-Known-Revision", revision.toString())
            }
            setBody(AddTodoRequest(element = todo))
        }.let {
            if (it.isSuccess) {
                val convert = it.getOrNull()?.wrapped<EditTodoResponse>()
                if (convert != null) {
                    ResultState.Success(convert)
                } else {
                    ResultState.Error("Null error couldn't convert")
                }
            } else {
                ResultState.Error(it.exceptionOrNull().toString())
            }
        }

    suspend fun editTodo(revision: Int, todo: TodoRequest) =
        webSource.tryPutResult("${NetworkConstants.PREFIX}/list/${todo.id}") {
            headers {
                append("Authorization", NetworkConstants.TOKEN)
                append("X-Last-Known-Revision", revision.toString())
            }
            setBody(EditTodoRequest(element = todo))
        }.let {
            if (it.isSuccess) {
                val convert = it.getOrNull()?.wrapped<GetTodoResponse>()
                if (convert != null) {
                    ResultState.Success(convert)
                } else {
                    ResultState.Error("Null error couldn't convert")
                }
            } else {
                ResultState.Error(it.exceptionOrNull().toString())
            }
        }

    suspend fun deleteTodo(revision: Int, todoId: String) =
        webSource.tryDeleteResult("${NetworkConstants.PREFIX}/list/$todoId") {
            headers {
                append("Authorization", NetworkConstants.TOKEN)
                append("X-Last-Known-Revision", revision.toString())
            }
        }.let {
            if (it.isSuccess) {
                val convert = it.getOrNull()?.wrapped<DeleteTodoResponse>()
                if (convert != null) {
                    ResultState.Success(convert)
                } else {
                    ResultState.Error("Null error couldn't convert")
                }
            } else {
                ResultState.Error(it.exceptionOrNull().toString())
            }
        }

    suspend fun getTodoById(revision: Int, todoId: String) =
        webSource.tryGetResult("${NetworkConstants.PREFIX}/list/$todoId") {
            headers {
                append("Authorization", NetworkConstants.TOKEN)
                append("X-Last-Known-Revision", revision.toString())
            }
        }.let {
            if (it.isSuccess) {
                val convert = it.getOrNull()?.wrapped<GetTodoResponse>()
                if (convert != null) {
                    ResultState.Success(convert)
                } else {
                    ResultState.Error("Null error couldn't convert")
                }
            } else {
                ResultState.Error(it.exceptionOrNull().toString())
            }
        }

    suspend fun updateList(revision: Int, todos: List<TodoRequest>) =
        webSource.tryPatchResult("${NetworkConstants.PREFIX}/list") {
            headers {
                append("Authorization", NetworkConstants.TOKEN)
                append("X-Last-Known-Revision", revision.toString())
            }
            setBody(todos)
        }.let {
            if (it.isSuccess) {
                val convert = it.getOrNull()?.wrapped<TodosResponse>()
                if (convert != null) {
                    ResultState.Success(convert)
                } else {
                    ResultState.Error("Null error couldn't convert")
                }
            } else {
                ResultState.Error(it.exceptionOrNull().toString())
            }
        }

}