package dev.flsrg.llmpollingclient.model.message.history

import dev.flsrg.llmpollingclient.client.ClientConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingDeque

class HistoryManager<T>(private val config: ClientConfig) {
    private val chatHistories = ConcurrentHashMap<String, LinkedBlockingDeque<T>>()

    fun getHistory(chatId: String): List<T> {
        return chatHistories[chatId]?.toList() ?: emptyList()
    }

    fun addMessage(chatId: String, message: T) {
        chatHistories
            .getOrPut(chatId) { LinkedBlockingDeque() }
            .apply {
                addLast(message)
                while (size > config.maxHistoryLength) removeFirst()
            }
    }

    fun clearHistory(chatId: String) {
        chatHistories.remove(chatId)
    }

    fun removeLast(chatId: String) {
        chatHistories[chatId]?.removeLast()
    }
}