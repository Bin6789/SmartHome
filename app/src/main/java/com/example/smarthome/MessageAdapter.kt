package com.example.smarthome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class MessageAdapter(
    private val messages: MutableList<Message>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderTextView: TextView = itemView.findViewById(R.id.message_sender)
        val messageTextView: TextView = itemView.findViewById(R.id.message_text)
        val timestampTextView: TextView = itemView.findViewById(R.id.message_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.senderTextView.text = "Sender: ${message.sender}"
        holder.messageTextView.text = message.text
        holder.timestampTextView.text = "Timestamp: ${
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(message.timestamp.toLongOrNull() ?: 0L)
        }"
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}