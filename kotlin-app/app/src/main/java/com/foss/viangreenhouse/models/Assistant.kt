package com.foss.viangreenhouse.models

enum class TabType {
    AI, GIT, RESEARCH
}

data class Assistant(
    val id: String,
    val name: String,
    val url: String,
    val icon: String,
    val tabType: TabType = TabType.AI
)

val defaultAssistants = listOf(
    Assistant("chatgpt", "ChatGPT", "https://chatgpt.com", "🤖"),
    Assistant("claude", "Claude", "https://claude.ai", "🧠"),
    Assistant("gemini", "Gemini", "https://gemini.google.com", "✨"),
    Assistant("perplexity", "Perplexity", "https://www.perplexity.ai", "🔍"),
    Assistant("deepseek", "DeepSeek", "https://chat.deepseek.com", "🌊"),
    Assistant("github", "GitHub", "https://github.com", "🐙", TabType.GIT),
    Assistant("duckduckgo", "Research", "https://duckduckgo.com", "🦆", TabType.RESEARCH)
)
