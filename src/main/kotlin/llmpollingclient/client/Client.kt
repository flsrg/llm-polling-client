package dev.flsrg.llmpollingclient.client

import dev.flsrg.llmpollingclient.model.ChatResponse
import dev.flsrg.llmpollingclient.model.message.history.HistoryManager
import kotlinx.coroutines.flow.Flow

abstract class Client(internal val config: ClientConfig) {
    abstract val histManager: HistoryManager

    abstract fun askChat(chatId: String, message: String, rememberHistory: Boolean = true): Flow<ChatResponse>

    open fun clearHistory(chatId: String) = histManager.clearHistory(chatId)
}