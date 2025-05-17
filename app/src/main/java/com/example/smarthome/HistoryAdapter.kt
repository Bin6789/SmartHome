package com.example.smarthome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val historyEntries: MutableList<HistoryEntry> = mutableListOf()
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val historyAction: TextView = itemView.findViewById(R.id.history_action_card)
        val historyTimestamp: TextView = itemView.findViewById(R.id.history_timestamp_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_card, parent, false)
        return HistoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyEntries[position]

        // Hiển thị hành động
        holder.historyAction.text = when (history.action) {
            "LED", "Relay" -> "Hành động: ${history.action} được ${if (history.state == true) "BẬT" else "TẮT"}"
            "Temperature" -> "Hành động: Nhiệt độ ${history.value ?: 0.0}°C"
            "Humidity" -> "Hành động: Độ ẩm ${history.value ?: 0.0}%"
            else -> "Hành động: ${history.action}"
        }

        // Xử lý timestamp (hỗ trợ ISO 8601)
        val timestampText = try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = isoFormat.parse(history.timestamp) ?: Date(0L)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            try {
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = isoFormat.parse(history.timestamp) ?: Date(0L)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                sdf.format(date)
            } catch (e: Exception) {
                history.timestamp
            }
        }
        holder.historyTimestamp.text = "Thời gian: $timestampText"
    }

    override fun getItemCount(): Int = historyEntries.size

    fun updateHistory(newHistory: List<HistoryEntry>) {
        val diffCallback = HistoryDiffCallback(historyEntries, newHistory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        historyEntries.clear()
        historyEntries.addAll(newHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    private class HistoryDiffCallback(
        private val oldList: List<HistoryEntry>,
        private val newList: List<HistoryEntry>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}