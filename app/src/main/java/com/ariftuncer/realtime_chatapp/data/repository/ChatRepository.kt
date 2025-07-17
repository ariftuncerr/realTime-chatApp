package com.ariftuncer.realtime_chatapp.data.repository

import com.ariftuncer.realtime_chatapp.data.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    fun addChat(friendUid: String, friendName: String, onComplete: (Boolean, String?) -> Unit) {
        val chat = Chat(
            uid = friendUid, friendName = friendName, timeStamp = System.currentTimeMillis(),
            profile = null
        )
        currentUid?.let {
            firestore.collection("users").document(it)
                .collection("chats")
                .document(friendUid)
                .set(chat)
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { e -> onComplete(false, e.message) }
        }
    }

    fun getChatList(onResult: (List<Chat>) -> Unit) {
        currentUid?.let {
            firestore.collection("users").document(it)
                .collection("chats")
                .addSnapshotListener { snapshot, _ ->
                    val list = snapshot?.toObjects(Chat::class.java) ?: emptyList()
                    onResult(list)
                }
        }
    }
}
