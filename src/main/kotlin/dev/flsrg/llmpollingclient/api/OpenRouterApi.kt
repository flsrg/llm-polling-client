package dev.flsrg.llmpollingclient.api

import dev.flsrg.llmpollingclient.Config
import dev.flsrg.llmpollingclient.client.ClientConfig
import dev.flsrg.llmpollingclient.client.OpenRouterClient
import dev.flsrg.llmpollingclient.client.OpenRouterClient.ChatRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory

class OpenRouterApi: Api {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun getCompletionsStream(config: ClientConfig, messagesJson: List<String>) = flow<HttpResponse> {
        val requestPayload = ChatRequest(
            model = config.model.id,
            chainOfThought = config.chainOfThoughts,
            stream = config.chainOfThoughts,
            messages = getMessages(messagesJson)
        )

        val messagesCount = getMessagesCount(requestPayload.messages)
        log.info("Requesting completions from OpenRouter " +
                "(${messagesCount.first} userMessages, ${messagesCount.second} assistantMessages)")

        Config.streamingClient.preparePost(config.baseUrl) {
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

    private fun getMessages(messagesJson: List<String>) = messagesJson.map {
        Config.format.decodeFromString<OpenRouterClient.ChatMessage>(it)
    }

    private fun getMessagesCount(messages: List<OpenRouterClient.ChatMessage>): Pair<Int, Int> {
        val userMessages = messages.count { it.role == "user" }
        val assistantMessages = messages.count { it.role == "assistant" }
        return Pair(userMessages, assistantMessages)
    }
}