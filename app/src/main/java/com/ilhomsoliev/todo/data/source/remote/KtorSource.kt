package com.ilhomsoliev.todo.data.source.remote

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
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
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpSendPipeline
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
import io.ktor.serialization.jackson.JacksonWebsocketContentConverter
import io.ktor.serialization.jackson.jackson
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

open class KtorSource {

    private val webSocketPingInterval = 20_000L

    private val baseClient by lazy {
        HttpClient(OkHttp) {

            expectSuccess = true

            engine {
                config {
                    //addInterceptor(chuckerInterceptor)
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
                level = LogLevel.BODY
                logger = LogAdapter
            }

            install(HttpRequestRetry) {
                maxRetries = 2
                exponentialDelay()
            }
            install(ContentNegotiation) {
                jackson {
                    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
//                    propertyNamingStrategy = SnakeCaseStrategy()
                    setSerializationInclusion(NON_NULL)
                }
            }
            defaultRequest {
                contentType(Json)
                headers {
                    append(
                        name = "Accept-Language",
                        value = "ru" //getDefault().language
                    )
                }
                host = "" // TODO NetworkConstants.HOST
                url {
                    protocol = URLProtocol.HTTP
                }
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 60_000L
                socketTimeoutMillis = INFINITE_TIMEOUT_MS
                requestTimeoutMillis = 60_000L
            }
//            install(UserAgent) { agent = env[ENV_USER_AGENT] ?: "" } TODO
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
                pingInterval = webSocketPingInterval
            }
        }/*.apply {
            plugin(HttpSend).intercept { request ->
                // Log.d("Hello Bear", "${env.get<Any>(ENV_BASE_URL)}.toString()")
                request.url.host = env[ENV_BASE_URL] ?: ""
                //request.url.protocol = URLProtocol.HTTPS
                execute(request)
            }
        }*/
    }

    val unauthorizedClient by lazy {
        baseClient.config {
            expectSuccess = false
        }
    }

    private var client = getClientWithTokens()

    private fun updateClientToken() {
        client = getClientWithTokens()
        unExpectClient = client.config { expectSuccess = false }
    }

    suspend fun wsSession(
        host: String, port: Int, path: String,
    ) = unauthorizedClient.webSocketSession(
        host = host,
        port = port,
        path = path,
    )

    suspend fun wssSession(
        host: String,
        path: String,
        block: suspend DefaultClientWebSocketSession.() -> Unit,
    ) = unauthorizedClient.wss(
        host = host,
        path = path,
        block = block,
    )

    fun closeClient() {
        unauthorizedClient.close()
        client.close()
        unExpectClient.close()
    }

    private fun getClientWithTokens(): HttpClient {
        baseClient.sendPipeline.intercept(HttpSendPipeline.State) {
            context.url({}) // TODO
        }
        return baseClient.config {
            install(Auth) {
                bearer {
                    loadTokens { BearerTokens("", "") } // TODO
                    refreshTokens { BearerTokens("", "") }
                }
            }
            defaultRequest {
                contentType(Json)
                headers {
                    append(
                        name = "Accept-Language",
                        value = "ru" // getDefault().language
                    )
                }
                host = "" // env[ENV_BASE_URL] ?: ""
            }
        }
    }

    private var unExpectClient =
        client.config { expectSuccess = false }

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

    suspend fun unauthorizedGetResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = runCatching {
        unauthorizedClient.get(url, block)
    }

    suspend fun unauthorizedPostResult(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = runCatching {
        unauthorizedClient.post(url, block)
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
    ): Result<HttpResponse> = updateClientToken().run {
        runCatching {
            unExpectClient.get(url, block)
        }
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
    ): Result<HttpResponse> = updateClientToken().run {
        runCatching {
            unExpectClient.patch(url, block)
        }
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
    ): Result<HttpResponse> = updateClientToken().run {
        runCatching {
            unExpectClient.delete(url, block)
        }
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
    ): Result<HttpResponse> = updateClientToken().run {
        runCatching {
            unExpectClient.post(url, block)
        }
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
    ): Result<HttpResponse> = updateClientToken().run {
        runCatching {
            unExpectClient.put(url, block)
        }
    }

    @Deprecated(
        "Use unauthorizedGetResult instead",
        ReplaceWith("unauthorizedGetResult(url, block)")
    )
    suspend fun unauthorizedGet(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = unauthorizedClient.get(url, block)

    @Deprecated(
        "Use unauthorizedPostResult instead",
        ReplaceWith("unauthorizedPostResult(url, block)")
    )
    suspend fun unauthorizedPost(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ) = unauthorizedClient.post(url, block)

    @Deprecated("Use tryGerResult instead", ReplaceWith("tryGetResult(url, block)"))
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