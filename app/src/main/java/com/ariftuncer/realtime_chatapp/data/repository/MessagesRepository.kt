package com.ariftuncer.realtime_chatapp.data.repository

import com.ariftuncer.realtime_chatapp.data.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.channels.awaitClose

class MessagesRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun getChatList(): Flow<List<Chat>> = callbackFlow {
        val currentUid = auth.currentUser?.uid ?: run {
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(currentUid)
            .collection("chats")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error)
                } else {
                    val chats = snapshot.toObjects(Chat::class.java)
                    trySend(chats)
                }
            }

        awaitClose { listener.remove() }
    }.catch { emit(emptyList()) }

    suspend fun deleteChat(friendUid: String): Pair<Boolean, String?> {
        val currentUid = auth.currentUser?.uid ?: return false to "Oturum bulunamadı"
        return try {
            // 1. Kullanıcının chat listesinden sil
            firestore.collection("users")
                .document(currentUid)
                .collection("chats")
                .document(friendUid)
                .delete()
                .await()

            // 2. direct_messages/chatId içeriğini sil
            val chatId = if (currentUid < friendUid) "${currentUid}_$friendUid" else "${friendUid}_$currentUid"
            firestore.collection("direct_messages")
                .document(chatId)
                .delete()
                .await()

            true to null
        } catch (e: Exception) {
            false to e.message
        }
    }


    fun addChat(friendUid: String, friendName: String): Flow<Pair<Boolean, String?>> = flow {
        val currentUid = auth.currentUser?.uid ?: run {
            emit(false to "Kullanıcı oturumu yok")
            return@flow
        }

        val currentUserName = auth.currentUser?.displayName ?: "Sen"

        val currentUserChat = Chat(uid = friendUid, friendName = friendName, profile = null)
        val friendUserChat = Chat(uid = currentUid, friendName = currentUserName, profile = null)

        val userRef = firestore.collection("users")

        val friendDoc = userRef.document(friendUid).get().await()
        if (!friendDoc.exists()) {
            emit(false to "Bu UID ile kayıtlı kullanıcı bulunamadı.")
            return@flow
        }

        userRef.document(currentUid)
            .collection("chats")
            .document(friendUid)
            .set(currentUserChat).await()

        userRef.document(friendUid)
            .collection("chats")
            .document(currentUid)
            .set(friendUserChat).await()

        emit(true to null)
    }.catch { e -> emit(false to (e.message ?: "Bilinmeyen hata")) }
}
