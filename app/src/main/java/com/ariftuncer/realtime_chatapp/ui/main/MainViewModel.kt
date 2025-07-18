package com.ariftuncer.realtime_chatapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.realtime_chatapp.data.model.Chat
import com.ariftuncer.realtime_chatapp.data.repository.MessagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = MessagesRepository()

    private val _chatList = MutableStateFlow<List<Chat>>(emptyList())
    val chatList: StateFlow<List<Chat>> = _chatList.asStateFlow()

    private val _addChatResult = MutableStateFlow<Pair<Boolean, String?>>(false to null)
    val addChatResult: StateFlow<Pair<Boolean, String?>> = _addChatResult.asStateFlow()

    fun loadChatList() {
        viewModelScope.launch {
            repository.getChatList().collect {
                _chatList.value = it
            }
        }
    }

    fun addChat(friendUid: String, friendName: String) {
        viewModelScope.launch {
            repository.addChat(friendUid, friendName).collect {
                _addChatResult.value = it
            }
        }
    }

    fun deleteChat(friendUid: String) {
        viewModelScope.launch {
            val result = repository.deleteChat(friendUid)
            if (result.first) {
                loadChatList() // listeyi yenile
            }
        }
    }

}
