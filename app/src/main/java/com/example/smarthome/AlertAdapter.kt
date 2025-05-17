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
        return AlertViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
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

        Log.d("AlertAdapter", "Binding alert at position $position: ${alert.deviceId}, ${alert.message}")
    }

    override fun getItemCount(): Int {
        Log.d("AlertAdapter", "Item count: ${alerts.size}")
        return alerts.size
    }

    fun updateAlerts(newAlerts: List<Alert>) {
        val diffCallback = AlertDiffCallback(alerts, newAlerts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        alerts.clear()
        alerts.addAll(newAlerts)
        diffResult.dispatchUpdatesTo(this)
        Log.d("AlertAdapter", "Updated alerts, new size: ${alerts.size}")
    }

    private class AlertDiffCallback(
        private val oldList: List<Alert>,
        private val newList: List<Alert>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}