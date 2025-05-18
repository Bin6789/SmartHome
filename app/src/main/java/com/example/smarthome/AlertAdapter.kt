package com.example.smarthome

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlertAdapter(
    private val alerts: MutableList<Alert> = mutableListOf()
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceIdText: TextView = itemView.findViewById(R.id.alert_device_id)
        val messageText: TextView = itemView.findViewById(R.id.alert_message)
        val statusText: TextView = itemView.findViewById(R.id.alert_status)
        val timestampText: TextView = itemView.findViewById(R.id.alert_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alert_card, parent, false)
        Log.d("AlertAdapter", "ViewHolder created for position")
        return AlertViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        Log.d("AlertAdapter", "Binding view holder at position $position")
        val alert = alerts[position]
        holder.deviceIdText.text = "Thiết bị: ${alert.deviceId}"
        holder.messageText.text = "Thông báo: ${alert.message}"
        holder.statusText.text = "Trạng thái: ${alert.status}"

        val timestampText = try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = isoFormat.parse(alert.timestamp) ?: Date(0L)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            alert.timestamp
        }
        holder.timestampText.text = "Thời gian: $timestampText"
    }

    override fun getItemCount(): Int {
        val count = alerts.size
        Log.d("AlertAdapter", "Item count: $count")
        return count
    }

    fun updateAlerts(newAlerts: List<Alert>) {
        Log.d("AlertAdapter", "Received ${newAlerts.size} alerts to update")
        val diffCallback = AlertDiffCallback(alerts, newAlerts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        alerts.clear()
        alerts.addAll(newAlerts)
        diffResult.dispatchUpdatesTo(this)
        Log.d("AlertAdapter", "DiffUtil applied, new size: ${alerts.size}")
        if (alerts.isNotEmpty()) {
            Log.d("AlertAdapter", "First alert after update: ${alerts[0]}")
        }
    }

    private class AlertDiffCallback(
        private val oldList: List<Alert>,
        private val newList: List<Alert>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val result = oldList.getOrNull(oldItemPosition)?.id == newList.getOrNull(newItemPosition)?.id
            Log.d("DiffCallback", "areItemsTheSame: old[$oldItemPosition].id=${oldList.getOrNull(oldItemPosition)?.id}, new[$newItemPosition].id=${newList.getOrNull(newItemPosition)?.id}, result=$result")
            return result
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)
            val result = oldItem == newItem
            Log.d("DiffCallback", "areContentsTheSame: old[$oldItemPosition]=$oldItem, new[$newItemPosition]=$newItem, result=$result")
            return result
        }
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)
            return if (oldItem != newItem) "UPDATE" else null
        }
    }
}