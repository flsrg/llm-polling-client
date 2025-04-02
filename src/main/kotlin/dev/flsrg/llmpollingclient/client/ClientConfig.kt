package dev.flsrg.llmpollingclient.client

sealed class ClientConfig(
    val baseUrl: String,
    val apiKey: String,
    val maxHistoryLength: Int = 10,
)

data class Model(val id: String, val reasoning: Boolean)

class OpenRouterConfig(
    baseUrl: String = "https://openrouter.ai/api/v1/chat/completions",
    apiKey: String,
    maxHistoryLength: Int = 20,
) : ClientConfig(baseUrl, apiKey, maxHistoryLength) {
    companion object {
        val DEEPSEEK_R1 = Model("deepseek/deepseek-r1:free", reasoning = true)
        val DEEPSEEK_V3 = Model("deepseek/deepseek-chat-v3-0324:free", reasoning = true)
    }
}