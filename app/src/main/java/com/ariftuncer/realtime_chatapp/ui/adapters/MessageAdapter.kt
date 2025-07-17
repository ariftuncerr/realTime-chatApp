package com.ariftuncer.realtime_chatapp.ui.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.realtime_chatapp.R
import com.ariftuncer.realtime_chatapp.data.model.Message

class MessageAdapter(
    private var messageList: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
        val senderName: TextView = view.findViewById(R.id.senderNameTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_card, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.messageText.text = message.message
        holder.senderName.text = if (message.senderId == currentUserId) "Sen" else "O"
    }

    override fun getItemCount(): Int = messageList.size

    fun updateMessages(newList: List<Message>) {
        messageList = newList
        notifyDataSetChanged()
    }
}
