package dev.flsrg.llmpollingclient.client

import dev.flsrg.llmpollingclient.model.ChatMessage
import dev.flsrg.llmpollingclient.model.ChatResponse
import kotlinx.coroutines.flow.Flow

abstract class Client(internal val config: ClientConfig) {
    abstract fun askChat(
        model: Model,
        messages: List<ChatMessage>,
        systemMessage: ChatMessage? = null,
    ): Flow<ChatResponse>
}