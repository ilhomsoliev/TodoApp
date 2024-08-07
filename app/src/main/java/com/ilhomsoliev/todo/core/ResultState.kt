package com.ilhomsoliev.todo.core

import com.ilhomsoliev.todo.data.shared.wrapped
import com.ilhomsoliev.todo.data.source.local.local_based.DataStoreManager
import io.ktor.client.statement.HttpResponse

sealed class ResultState<out T> {

    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(
        val message: String = "",
        val types: List<ErrorTypes> = listOf(ErrorTypes.Unspecified)
    ) : ResultState<Nothing>() {
        constructor(message: String = "", vararg types: ErrorTypes) : this(message, types.toList())
    }
}

suspend fun <T> ResultState<T>.on(
    success: suspend (T) -> Unit = {},
    error: suspend (ResultState.Error) -> Unit = {}
): ResultState<T> {
    when (this) {
        is ResultState.Success -> success(this.data)
        is ResultState.Error -> error(this)
    }
    return this
}

inline fun <T, R> ResultState<T>.map(transform: (T) -> R): ResultState<R> {
    return when (this) {
        is ResultState.Success -> ResultState.Success(transform(this.data))
        is ResultState.Error -> this
    }
}

suspend inline fun <reified T> Result<HttpResponse>.toResultState(): ResultState<T> {
    return if (this.isSuccess) {
        val convert = this.getOrNull()?.wrapped<T>()
        if (convert != null) {
            ResultState.Success(convert)
        } else {
            DataStoreManager.hasError.value = true
            ResultState.Error("Null error couldn't convert", types = listOf(ErrorTypes.Network))
        }
    } else {
        DataStoreManager.hasError.value = true
        ResultState.Error(
            this.exceptionOrNull()?.toString() ?: "Unknown error",
            types = listOf(ErrorTypes.Network)
        )
    }
}

sealed class ErrorTypes(val message: String = "") {

    data object Unspecified : ErrorTypes("")
    sealed class Validation : ErrorTypes("Validation Error") {
        data object Email : Validation()
        data object Password : Validation()
        data object Phone : Validation()
        data object Name : Validation()
        data object Lastname : Validation()
    }

    data object Network : ErrorTypes("Network problem")


}