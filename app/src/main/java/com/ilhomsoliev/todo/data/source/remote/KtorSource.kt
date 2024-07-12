package com.ilhomsoliev.todo.data.source.remote

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.ilhomsoliev.todo.core.NetworkConstants
import com.ilhomsoliev.todo.core.NetworkConstants.TOKEN
import com.ilhomsoliev.todo.data.source.LogAdapter
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeout.Plugin.INFINITE_TIMEOUT_MS
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.URLProtocol
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.jackson.jackson
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

open class KtorSource {

    private val webSocketPingInterval = 20_000L

    private val baseClient by lazy {
        HttpClient(OkHttp) {
            expectSuccess = false
            engine {
                config {
                    writeTimeout(60, SECONDS)
                }
                preconfigured = OkHttpClient.Builder()
                    .pingInterval(
                        webSocketPingInterval,
                        MILLISECONDS
                    )
                    .retryOnConnectionFailure(true)
                    .build()
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = LogAdapter
            }
            install(Auth){
                headers {
                    bearer { loadTokens { BearerTokens(TOKEN, TOKEN) } }
                }
            }

            install(HttpRequestRetry) {
                maxRetries = 2
                exponentialDelay()
            }
            install(ContentNegotiation) {
                jackson {
                    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                    setSerializationInclusion(NON_NULL)
                }
            }
            defaultRequest {
                contentType(Json)
                headers {
                    append("Authorization", NetworkConstants.OAUTH)
                }
                host = NetworkConstants.HOST
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 60_000L
                socketTimeoutMillis = INFINITE_TIMEOUT_MS
                requestTimeoutMillis = 60_000L
            }
        }
    }

    private var client = getClientWithTokens()

    private fun updateClientToken() {
        client = getClientWithTokens()
        unExpectClient = client.config { expectSuccess = false }
    }

    private fun getClientWithTokens(): HttpClient {
        return baseClient.config {

        }
    }

    private var unExpectClient =
        client.config {
            expectSuccess = false

        }

    suspend fun delete(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        client.delete(url, block)
    }

    suspend fun post(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        client.post(url, block)
    }


    /**
     * Выполняет HTTP GET запрос к указанному URL с возможностью выполнения дополнительных настроек с использованием блока [block].
     *
     * @param url URL для выполнения GET запроса.
     * @param block Блок с дополнительными настройками запроса, который можно использовать по умолчанию.
     *
     * @return Результат выполнения GET запроса в виде [Result] с объектом [HttpResponse] или ошибкой.
     */
    suspend fun tryGetResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> = try {
        updateClientToken().run {
            runCatching {
                unExpectClient.get(url, block)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Выполняет HTTP PATCH запрос к указанному URL с возможностью выполнения дополнительных настроек с использованием блока [block].
     *
     * @param url URL для выполнения PATCH запроса.
     * @param block Блок с дополнительными настройками запроса, который можно использовать по умолчанию.
     *
     * @return Результат выполнения PATCH запроса в виде [Result] с объектом [HttpResponse] или ошибкой.
     */
    suspend fun tryPatchResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> = try {
        updateClientToken().run {
            runCatching {
                unExpectClient.patch(url, block)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Выполняет HTTP DELETE запрос к указанному URL с возможностью выполнения дополнительных настроек с использованием блока [block].
     *
     * @param url URL для выполнения DELETE запроса.
     * @param block Блок с дополнительными настройками запроса, который можно использовать по умолчанию.
     *
     * @return Результат выполнения DELETE запроса в виде [Result] с объектом [HttpResponse] или ошибкой.
     */
    suspend fun tryDeleteResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> = try {
        updateClientToken().run {
            runCatching {
                unExpectClient.delete(url, block)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Выполняет HTTP POST запрос с передачей формы с данными в виде [PartData] к указанному URL с возможностью выполнения дополнительных настроек с использованием блока [block].
     *
     * @param url URL для выполнения POST запроса.
     * @param formData Список [PartData] с данными для отправки в теле запроса.
     * @param block Блок с дополнительными настройками запроса, который можно использовать по умолчанию.
     *
     * @return Результат выполнения POST запроса в виде [Result] с объектом [HttpResponse] или ошибкой.
     */
    suspend fun tryPostFormDataResult(
        url: String,
        formData: List<PartData>,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> = updateClientToken().run {
        runCatching {
            unExpectClient.submitFormWithBinaryData(url, formData, block)
        }
    }

    /**
     * Выполняет HTTP POST запрос к указанному URL с возможностью выполнения дополнительных настроек с использованием блока [block].
     *
     * @param url URL для выполнения POST запроса.
     * @param block Блок с дополнительными настройками запроса, который можно использовать по умолчанию.
     *
     * @return Результат выполнения POST запроса в виде [Result] с объектом [HttpResponse] или ошибкой.
     */
    suspend fun tryPostResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> = try {
        updateClientToken().run {
            runCatching {
                unExpectClient.post(url, block)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Выполняет HTTP PUT запрос к указанному URL с возможностью выполнения дополнительных настроек с использованием блока [block].
     *
     * @param url URL для выполнения PUT запроса.
     * @param block Блок с дополнительными настройками запроса, который можно использовать по умолчанию.
     *
     * @return Результат выполнения PUT запроса в виде [Result] с объектом [HttpResponse] или ошибкой.
     */
    suspend fun tryPutResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> = try {
        updateClientToken().run {
            runCatching {
                unExpectClient.put(url, block)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun tryGet(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        unExpectClient.get(url, block)
    }

    @Deprecated("Use tryPostResult instead", ReplaceWith("tryPostResult(url, block)"))
    suspend fun tryPost(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        unExpectClient.post(url, block)
    }

    @Deprecated(
        "Use tryPostFormDataResult instead",
        ReplaceWith("tryPostFormDataResult(url, formData, block)")
    )
    suspend fun tryPostFormData(
        url: String,
        formData: List<PartData>,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        unExpectClient.submitFormWithBinaryData(
            url, formData, block
        )
    }

    @Deprecated("Use tryPatchResult instead", ReplaceWith("tryPatchResult(url, block)"))
    suspend fun tryPatch(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        unExpectClient.patch(url, block)
    }

    @Deprecated("Use tryDeleteResult instead", ReplaceWith("tryDeleteResult(url, block)"))
    suspend fun tryDelete(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        unExpectClient.delete(url, block)
    }

    @Deprecated("Use tryPutResult instead", ReplaceWith("tryPutResult(url, block)"))
    suspend fun tryPut(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = updateClientToken().let {
        unExpectClient.put(url, block)
    }
}