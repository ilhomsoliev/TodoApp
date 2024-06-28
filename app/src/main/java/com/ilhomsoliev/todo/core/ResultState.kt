package com.ilhomsoliev.todo.core

sealed class ResultState<out T> {

    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(
        val message: String = "",
        val types: List<ErrorTypes> = listOf(ErrorTypes.Unspecified)
    ) : ResultState<Nothing>() {
        constructor(message: String = "", vararg types: ErrorTypes) : this(message, types.toList())
    }
}

fun <T> ResultState<T>.on(success: (T) -> Unit, error: (ResultState.Error) -> Unit): ResultState<T> {
    when (this) {
        is ResultState.Success -> success(this.data)
        is ResultState.Error -> error(this )
    }
    return this
}

inline fun <T, R> ResultState<T>.map(transform: (T) -> R): ResultState<R> {
    return when (this) {
        is ResultState.Success -> ResultState.Success(transform(this.data))
        is ResultState.Error -> this
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


}