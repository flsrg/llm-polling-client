package dev.flsrg.llmpollingclient.client

import dev.flsrg.llmpollingclient.Config
import dev.flsrg.llmpollingclient.api.OpenRouterApi
import dev.flsrg.llmpollingclient.model.ChatMessage
import dev.flsrg.llmpollingclient.model.ChatResponse
import dev.flsrg.llmpollingclient.repository.OpenRouterRepository
import kotlinx.coroutines.flow.Flow

class OpenRouterClient(config: ClientConfig): Client(config) {
    private val api = OpenRouterApi()
    private val repository = OpenRouterRepository(api)

    override fun askChat(model: Model, messages: List<ChatMessage>, systemMessage: ChatMessage?): Flow<ChatResponse> {
        val payload: List<String> = if (systemMessage != null) {
            listOf(systemMessage) + messages
        } else {
            messages
        }.map {
            Config.format.encodeToString(it)
        }

        return repository.getCompletionsStream(config, model, payload) {
            Config.format.decodeFromString<ChatResponse>(it)
        }
    }
}