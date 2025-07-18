
package com.ariftuncer.realtime_chatapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.realtime_chatapp.R
import com.ariftuncer.realtime_chatapp.data.model.Chat

class ChatAdapter(
    private var chatList: List<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatCard: CardView = itemView.findViewById(R.id.chatCard)
        val chatProfile: ImageButton = itemView.findViewById(R.id.chatProfileBtn)
        val chatName: TextView = itemView.findViewById(R.id.chatNameTxt)

        fun bind(chat: Chat) {
            chatName.text = chat.friendName

            chatCard.setOnClickListener {
                onItemClick(chat)
            }

            chatProfile.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_channel_card, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int = chatList.size

    fun updateList(newList: List<Chat>) {
        chatList = newList
        notifyDataSetChanged()
    }
}

