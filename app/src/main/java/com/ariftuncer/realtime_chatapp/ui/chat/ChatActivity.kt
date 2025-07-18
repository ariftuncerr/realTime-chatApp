package com.ariftuncer.realtime_chatapp.ui.chat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariftuncer.realtime_chatapp.data.model.Message
import com.ariftuncer.realtime_chatapp.databinding.ActivityChatBinding
import com.ariftuncer.realtime_chatapp.ui.adapters.MessageAdapter
import com.google.firebase.auth.FirebaseAuth

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()

    private lateinit var adapter: MessageAdapter
    private lateinit var friendUid: String
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Chat başlatılacak UID
        friendUid = intent.getStringExtra("friendUid") ?: ""

        setupRecyclerView()
        setupObservers()
        viewModel.startListeningMessages(friendUid)

        binding.sendMsgButton.setOnClickListener {
            val messageText = binding.messageInputEditTxt.text.toString()
            if (messageText.isNotBlank()) {
                viewModel.sendMessage(friendUid, messageText)
                binding.messageInputEditTxt.setText("")
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(emptyList(), currentUid)
        binding.messageRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.messageRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.messages.observe(this) { messageList ->
            adapter.updateMessages(messageList)
            binding.messageRecyclerView.scrollToPosition(messageList.size - 1)
        }

        viewModel.sendResult.observe(this) { (success, errorMsg) ->
            if (!success) {
                Toast.makeText(this, "Mesaj gönderilemedi: $errorMsg", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
