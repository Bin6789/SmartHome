package com.example.smarthome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    private val notifications: MutableList<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.notification_message)
        val timestampTextView: TextView = itemView.findViewById(R.id.notification_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.messageTextView.text = notification.message
        holder.timestampTextView.text = "Timestamp: ${
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(notification.timestamp.toLongOrNull() ?: 0L)
        }"
    }

    override fun getItemCount(): Int = notifications.size

    fun updateNotifications(newNotifications: List<NotificationItem>) {
        notifications.clear()
        notifications.addAll(newNotifications)
        notifyDataSetChanged()
    }
}