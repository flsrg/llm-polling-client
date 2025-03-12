package dev.flsrg.llmpollingclient.client

import dev.flsrg.llmpollingclient.Config
import dev.flsrg.llmpollingclient.api.OpenRouterApi
import dev.flsrg.llmpollingclient.client.OpenRouterClient.ChatMessage
import dev.flsrg.llmpollingclient.client.OpenRouterClient.ChatResponse
import dev.flsrg.llmpollingclient.model.message.history.HistoryManager
import dev.flsrg.llmpollingclient.repository.OpenRouterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class OpenRouterClient(config: ClientConfig): Client<ChatResponse, ChatMessage>(config) {
    private val api = OpenRouterApi()
    private val repository = OpenRouterRepository(api)
    override val histManager = HistoryManager<ChatMessage>(config)

    override fun askChat(chatId: String, message: String, rememberHistory: Boolean): Flow<ChatResponse> {
        val newMessage = ChatMessage(role = "user", content = message)

        val messages: List<ChatMessage> = if (rememberHistory) {
            histManager.addMessage(chatId, newMessage)
            histManager.getHistory(chatId)
        } else {
            listOf(newMessage)
        }

        val payload: List<String> = messages.map {
            Config.format.encodeToString(it)
        }

        val contentBuffer = StringBuilder()

        return repository.getCompletionsStream<ChatResponse>(config, payload) {
            transform(it)
        }.onEach { response ->
            val content = response.choices.first().delta?.content
            if (rememberHistory && content != null) {
                contentBuffer.append(response.choices.first().delta?.content)
            }
        }.onCompletion { exception ->
            if (exception != null) throw exception

            if (rememberHistory) {
                val assistantMessage = ChatMessage(role = "assistant", content = contentBuffer.toString())
                if (assistantMessage.content.isEmpty()) {
                    histManager.removeLast(chatId)
                    throw ExceptionEmptyResponse()
                }

                histManager.addMessage(chatId, assistantMessage)
                contentBuffer.clear()
            }
        }
    }

    private fun transform(response: String): ChatResponse = Config.format.decodeFromString<ChatResponse>(response)

    class ExceptionEmptyResponse() : Exception("Empty response")

    @Serializable
    data class ChatMessage(
        val role: String,
        val content: String,
    )

    @Serializable
    data class ChatRequest(
        val model: String,
        @SerialName("chain_of_thought")
        val chainOfThought: Boolean = true,
        val messages: List<ChatMessage>,
        val stream: Boolean = true
    )

    @Serializable
    data class ChatResponse(
        val provider: String,
        val choices: List<StreamChoice>,
    )

    @Serializable
    data class StreamChoice(
        val delta: Delta? = null,
    )

    @Serializable
    data class Delta(
        val content: String? = null,
        val reasoning: String? = null,
    )
}