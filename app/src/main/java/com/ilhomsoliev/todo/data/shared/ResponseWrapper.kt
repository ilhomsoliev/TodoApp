package com.ilhomsoliev.todo.data.shared

import com.fasterxml.jackson.annotation.JsonAlias
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.*
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

enum class Status {
    @JsonAlias("success")
    SUCCESS,

    @JsonAlias("error")
    @Suppress("unused")
    ERROR
}

data class ResponseWrapperTest<T : Any?>(
    val status: Status? = null,
    val data: T,
    val error: Error? = null,
    val paginator: Paginator? = null,
) {

    @Suppress("unused")
    class Paginator(
        val type: String,
        val total: Int,
        val perPage: Int,
        val currentPage: Int,
        val last_page: Int,
    ) {

        constructor() : this(
            (""), (0), (0),
            (0), (0)
        )
    }

    data class Error(
        val code: String? = null,
        val message: String? = null,
        val exception: String? = null,
        val file: String? = null,
        val line: Int? = null,
        val trace: Any? = null,
    )
}

data class ResponseWrapper<T : Any?>(
    val status: Status,
    val data: T,
    val error: Error? = null,
    val paginator: Paginator? = null,
) {

    class Paginator(
        val type: String,
        val total: Int,
        val perPage: Int,
        val currentPage: Int,
        val last_page: Int,
    )

    data class Error(
        val code: String? = null,
        val message: String? = null,
        val exception: String? = null,
        val file: String? = null,
        val line: Int? = null,
        val trace: Any? = null,
    )

}

data class ErrorResponseWrapper(
    val status: String = "",
    val response: String = "",
    val error: Error = Error(),
) {
    data class Error(
        val code: String = "",
        val message: String = "",
        val exception: String = "",
        val file: String = "",
        val line: Int = 0,
        val trace: Any? = null,
    )
}

suspend inline fun <reified T> HttpResponse.paginateWrapped(
): Pair<T, ResponseWrapper.Paginator> where T : Any? {
    val response = body<ResponseWrapper<T>>()
    return Pair(response.data, response.paginator!!)
}

suspend inline fun HttpResponse.errorWrapped() =
    body<ErrorResponseWrapper>()

suspend inline fun <reified T> HttpResponse.wrappedTest(): T
        where T : Any? = body<ResponseWrapperTest<T>>().data

suspend inline fun <reified T> HttpResponse.wrapped(): T
        where T : Any? = body()

private fun String?.errorController(): String {
    return this ?: "Неизвестная ошибка"
}

