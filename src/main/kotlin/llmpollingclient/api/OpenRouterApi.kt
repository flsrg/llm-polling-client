package dev.flsrg.llmpollingclient.api

import dev.flsrg.llmpollingclient.Config
import dev.flsrg.llmpollingclient.client.ClientConfig
import dev.flsrg.llmpollingclient.model.ChatMessage
import dev.flsrg.llmpollingclient.model.ChatRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory

class OpenRouterApi: Api {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun getCompletionsStream(config: ClientConfig, messages: List<ChatMessage>) = flow<HttpResponse> {
        val requestPayload = ChatRequest(
            model = config.model.id,
            chainOfThought = config.chainOfThoughts,
            stream = config.chainOfThoughts,
            messages = messages.toList()
        )

        log.info("Requesting completions from OpenRouter (payload: {})", requestPayload)

        Config.sreamingClient.preparePost(config.baseUrl) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${config.apiKey}")
                append(HttpHeaders.ContentType, "application/json")
                if (config.chainOfThoughts) append(HttpHeaders.Accept, "text/event-stream")
            }
            setBody(requestPayload)
        }.execute { response ->
            emit(response)
        }
    }
}