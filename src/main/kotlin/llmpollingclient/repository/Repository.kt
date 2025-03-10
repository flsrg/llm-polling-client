package dev.flsrg.llmpollingclient.repository

import dev.flsrg.llmpollingclient.client.ClientConfig
import dev.flsrg.llmpollingclient.model.ChatMessage
import dev.flsrg.llmpollingclient.model.ChatResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCompletionsStream(config: ClientConfig, chatMessages: List<ChatMessage>): Flow<ChatResponse>
}