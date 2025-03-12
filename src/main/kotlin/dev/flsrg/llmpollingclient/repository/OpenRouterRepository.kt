package dev.flsrg.llmpollingclient.repository

import dev.flsrg.llmpollingclient.api.Api
import dev.flsrg.llmpollingclient.client.ClientConfig
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.slf4j.LoggerFactory

class OpenRouterRepository(private val api: Api): Repository {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun <T> getCompletionsStream(
        config: ClientConfig,
        chatMessages: List<String>,
        transform: (String) -> T,
    ): Flow<T> {
        return flow<T> {
            api.getCompletionsStream(config, chatMessages).collect { response ->
                log.info("Received API response (code={})", response.status.value)

                val channel: ByteReadChannel = response.bodyAsChannel()
                val isScopeActive = currentCoroutineContext().isActive

                while (!channel.isClosedForRead && isScopeActive) {
                    val line = channel.readUTF8Line() ?: break

                    if (line.startsWith("data: ")) {
                        val json = line.removePrefix("data: ").trim()
                        if (json == "[DONE]") break

                        emit(transform(json))
                    }
                }
            }
        }
    }
}