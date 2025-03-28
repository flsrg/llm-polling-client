package dev.flsrg.llmpollingclient

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory

object Config {
    const val API_CONNECTION_TIMEOUT_MS = 5 * 60 * 1000L

    val format = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val streamingClient = HttpClient(OkHttp) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(format)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS
            socketTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS
            connectTimeoutMillis = API_CONNECTION_TIMEOUT_MS
        }
        install(SSE)
        engine {
            config {
                socketFactory(SocketFactory.getDefault())
                callTimeout(0, TimeUnit.MILLISECONDS)
                connectTimeout(API_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                readTimeout(0, TimeUnit.MILLISECONDS)
                writeTimeout(0, TimeUnit.MILLISECONDS)
            }
        }
    }

    enum class Model(val id: String) {
        DEEPSEEK_R1("deepseek/deepseek-r1:free")
    }
}