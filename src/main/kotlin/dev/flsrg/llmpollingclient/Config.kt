package dev.flsrg.llmpollingclient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object Config {
    const val API_CONNECTION_TIMEOUT_MS = 5 * 60 * 1000L

    val format = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val streamingClient = HttpClient(CIO) {
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
            requestTimeout = 0
            endpoint {
                connectTimeout = API_CONNECTION_TIMEOUT_MS
                socketTimeout = 0
            }
        }
    }
}