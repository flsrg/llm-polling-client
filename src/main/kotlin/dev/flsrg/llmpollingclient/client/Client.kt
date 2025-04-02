package dev.flsrg.llmpollingclient.client

import dev.flsrg.llmpollingclient.model.message.history.HistoryManager
import kotlinx.coroutines.flow.Flow

abstract class Client<T, R>(internal val config: ClientConfig) {
    abstract val histManager: HistoryManager<R>

    abstract fun askChat(
        chatId: String,
        model: Model,
        message: String,
        rememberHistory: Boolean = true,
        systemMessage: String? = null,
    ): Flow<T>

    open fun clearHistory(chatId: String) = histManager.clearHistory(chatId)
}