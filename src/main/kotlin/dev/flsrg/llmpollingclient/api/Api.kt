package dev.flsrg.llmpollingclient.api

import dev.flsrg.llmpollingclient.client.ClientConfig
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface Api {
    fun getCompletionsStream(config: ClientConfig, messagesJson: List<String>): Flow<HttpResponse>
}