package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter
    private val historyEntries = mutableListOf<HistoryEntry>()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyAdapter = HistoryAdapter(historyEntries)
        recyclerView.adapter = historyAdapter

        val deviceId = arguments?.getString("deviceId") ?: "DEV001"
        database.reference.child("SmartHomeApp/history/$deviceId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val history = snapshot.children.mapNotNull {
                        val action = it.child("action").getValue(String::class.java) ?: ""
                        val state = it.child("state").getValue(Boolean::class.java) ?: false
                        val timestamp = it.child("timestamp").getValue(String::class.java) ?: ""
                        HistoryEntry(action, state, timestamp)
                    }.sortedByDescending { it.timestamp.toLongOrNull() ?: 0L }
                    historyAdapter.updateHistory(history)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return view
    }
}