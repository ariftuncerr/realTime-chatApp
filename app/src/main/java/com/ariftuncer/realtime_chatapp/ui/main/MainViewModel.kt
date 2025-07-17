package com.ariftuncer.realtime_chatapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariftuncer.realtime_chatapp.data.model.Chat
import com.ariftuncer.realtime_chatapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> = _chatList

    private val _addChatResult = MutableLiveData<Pair<Boolean, String?>>()
    val addChatResult: LiveData<Pair<Boolean, String?>> = _addChatResult

    private val authRepository = AuthRepository()

    fun loadChatList() {
        val currentUid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(currentUid)
            .collection("chats")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val chats = snapshot.toObjects(Chat::class.java)
                _chatList.postValue(chats)
            }
    }

    fun addChat(friendUid: String, friendName: String) {
        val currentUid = auth.currentUser?.uid ?: return
        val currentUserName = auth.currentUser?.displayName ?: "Sen"

        val currentUserChat = Chat(friendUid, friendName)
        val friendUserChat = Chat(currentUid, currentUserName)

        val userRef = firestore.collection("users")

        // Önce karşı taraf gerçekten kayıtlı mı kontrol et
        userRef.document(friendUid).get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                _addChatResult.postValue(Pair(false, "Bu UID ile kayıtlı kullanıcı bulunamadı."))
                return@addOnSuccessListener
            }

            // 1. Benim chat listeme onu ekle
            userRef.document(currentUid)
                .collection("chats")
                .document(friendUid)
                .set(currentUserChat)

            // 2. Onun chat listesine beni ekle
            userRef.document(friendUid)
                .collection("chats")
                .document(currentUid)
                .set(friendUserChat)

            _addChatResult.postValue(Pair(true, null))
        }.addOnFailureListener {
            _addChatResult.postValue(Pair(false, "Bir hata oluştu: ${it.message}"))
        }
    }
}
