package dev.flsrg.llmpollingclient.model.message.history

import dev.flsrg.llmpollingclient.client.ClientConfig
import dev.flsrg.llmpollingclient.model.ChatMessage
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingDeque

class HistoryManager(private val config: ClientConfig) {
    private val chatHistories = ConcurrentHashMap<String, LinkedBlockingDeque<ChatMessage>>()

    fun getHistory(chatId: String): List<ChatMessage> {
        return chatHistories[chatId]?.toList() ?: emptyList()
    }

    fun addMessage(chatId: String, message: ChatMessage) {
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