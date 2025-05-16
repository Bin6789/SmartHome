package com.example.smarthome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val historyEntries: MutableList<HistoryEntry>
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
        holder.historyAction.text = "Action: ${history.action} turned ${if (history.state) "ON" else "OFF"}"
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.historyTimestamp.text = "Timestamp: ${sdf.format(history.timestamp.toLongOrNull() ?: 0L)}"
    }

    override fun getItemCount(): Int = historyEntries.size

    fun updateHistory(newHistory: List<HistoryEntry>) {
        historyEntries.clear()
        historyEntries.addAll(newHistory)
        notifyDataSetChanged()
    }
}