package dev.flsrg.llmpollingclient.repository

import dev.flsrg.llmpollingclient.client.ClientConfig
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun <T> getCompletionsStream(
        config: ClientConfig,
        chatMessages: List<String>,
        transform: (String) -> T,
    ): Flow<T>
}