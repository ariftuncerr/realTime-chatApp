package com.ariftuncer.realtime_chatapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariftuncer.realtime_chatapp.data.model.Message
import com.ariftuncer.realtime_chatapp.data.repository.MessageRepository

class ChatViewModel : ViewModel() {

    private val repository = MessageRepository()

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _sendResult = MutableLiveData<Pair<Boolean, String?>>()
    val sendResult: LiveData<Pair<Boolean, String?>> = _sendResult

    fun sendMessage(receiverId: String, text: String) {
        repository.sendMessage(receiverId, text) { success, errorMsg ->
            _sendResult.postValue(Pair(success, errorMsg))
        }
    }

    fun startListeningMessages(friendUid: String) {
        repository.listenMessages(friendUid) { messageList ->
            _messages.postValue(messageList)
        }
    }
}
