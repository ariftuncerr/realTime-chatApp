package com.ariftuncer.realtime_chatapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariftuncer.realtime_chatapp.databinding.FragmentMessagesBinding
import com.ariftuncer.realtime_chatapp.ui.adapters.ChatAdapter
import com.ariftuncer.realtime_chatapp.ui.chat.ChatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()
        setupUIEvents()

        mainViewModel.loadChatList()

        return binding.root
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(emptyList()) { chat ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("friendUid", chat.uid)
            intent.putExtra("friendName", chat.friendName)
            startActivity(intent)
        }

        binding.chatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatsRecyclerView.adapter = chatAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.chatList.collectLatest { list ->
                chatAdapter.updateList(list)
            }
        }

        lifecycleScope.launch {
            mainViewModel.addChatResult.collectLatest { (success, message) ->
                if (success) {
                    Toast.makeText(requireContext(), "Arkadaş eklendi", Toast.LENGTH_SHORT).show()
                    binding.newFriendClayout.visibility = View.GONE
                    binding.friendIdEditTxt.text?.clear()
                    binding.friendNameEditTxt.text?.clear()
                } else {
                    Toast.makeText(requireContext(), "Hata: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupUIEvents() {
        binding.newFriendBtn.setOnClickListener {
            binding.newFriendClayout.visibility =
                if (binding.newFriendClayout.isVisible) View.GONE else View.VISIBLE
        }

        binding.addFriendBtn.setOnClickListener {
            val friendUid = binding.friendIdEditTxt.text.toString().trim()
            val friendName = binding.friendNameEditTxt.text.toString().trim()

            if (friendUid.isNotEmpty() && friendName.isNotEmpty()) {
                mainViewModel.addChat(friendUid, friendName)
            } else {
                Snackbar.make(binding.root, "Lütfen UID ve isim giriniz", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}