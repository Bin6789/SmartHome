package com.example.smarthome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter
    private val historyEntries = mutableListOf<HistoryEntry>()
    private lateinit var deviceViewModel: DeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyAdapter = HistoryAdapter(historyEntries)
        recyclerView.adapter = historyAdapter

        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        deviceViewModel.historyData.observe(viewLifecycleOwner) { history ->
            historyAdapter.updateHistory(history)
            Log.d("HistoryFragment", "History updated: ${history.size} entries")
        }

        val deviceId = arguments?.getString("deviceId") ?: "DEV001"
        deviceViewModel.observeHistory(deviceId)

        return view
    }
}