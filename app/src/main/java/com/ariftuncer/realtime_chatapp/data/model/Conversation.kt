package com.ariftuncer.realtime_chatapp.data.model

data class Conversation (
    val conversationId: String = "",
    val users: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)