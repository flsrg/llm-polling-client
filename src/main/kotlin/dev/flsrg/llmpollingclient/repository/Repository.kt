package dev.flsrg.llmpollingclient.repository

import dev.flsrg.llmpollingclient.client.ClientConfig
import dev.flsrg.llmpollingclient.client.Model
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun <T> getCompletionsStream(
        config: ClientConfig,
        model: Model,
        chatMessages: List<String>,
        transform: (String) -> T,
    ): Flow<T>
}