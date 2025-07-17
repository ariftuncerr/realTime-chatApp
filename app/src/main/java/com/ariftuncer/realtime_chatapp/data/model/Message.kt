package com.ariftuncer.realtime_chatapp.data.model
data class Message (
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)