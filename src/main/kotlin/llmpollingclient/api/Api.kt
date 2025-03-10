package dev.flsrg.llmpollingclient.api

import dev.flsrg.llmpollingclient.client.ClientConfig
import dev.flsrg.llmpollingclient.model.ChatMessage
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface Api {
    fun getCompletionsStream(config: ClientConfig, messages: List<ChatMessage>): Flow<HttpResponse>
}