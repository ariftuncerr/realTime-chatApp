package com.ariftuncer.realtime_chatapp.data.repository

import com.ariftuncer.realtime_chatapp.data.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MessageRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun generateChatId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) "${uid1}_$uid2" else "${uid2}_$uid1"
    }

    fun sendMessage(
        receiverId: String,
        messageText: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val senderId = auth.currentUser?.uid ?: return onResult(false, "Kullanıcı yok")
        val chatId = generateChatId(senderId, receiverId)

        val message = Message(
            messageId = UUID.randomUUID().toString(),
            senderId = senderId,
            receiverId = receiverId,
            message = messageText
        )

        firestore.collection("direct_messages")
            .document(chatId)
            .collection("messages")
            .document(message.messageId)
            .set(message)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.message) }
    }

    fun listenMessages(
        friendUid: String,
        onUpdate: (List<Message>) -> Unit
    ) {
        val currentUid = auth.currentUser?.uid ?: return
        val chatId = generateChatId(currentUid, friendUid)

        firestore.collection("direct_messages")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    onUpdate(emptyList())
                    return@addSnapshotListener
                }

                val messages = snapshot.toObjects(Message::class.java)
                onUpdate(messages)
            }
    }
}
