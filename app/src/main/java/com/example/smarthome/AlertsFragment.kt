package com.example.smarthome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log

class AlertsFragment : Fragment() {

    private lateinit var alertAdapter: AlertAdapter
    private val alerts = mutableListOf<Alert>()
    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")
    private var alertListener: ValueEventListener? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alerts, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.alerts_recycler_view)
        val totalAlertsCountText = view.findViewById<TextView>(R.id.total_alerts_count)

        if (recyclerView == null) {
            Log.e("AlertsFragment", "RecyclerView is null, check fragment_alerts.xml for ID alerts_recycler_view")
            return view
        }

        if (totalAlertsCountText == null) {
            Log.e("AlertsFragment", "TextView total_alerts_count is null, check fragment_alerts.xml for ID total_alerts_count")
            return view
        }

        if (context != null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            Log.e("AlertsFragment", "Context is null, cannot set LayoutManager")
            return view
        }
        alertAdapter = AlertAdapter(alerts)
        recyclerView.adapter = alertAdapter
        Log.d("AlertsFragment", "RecyclerView adapter set with ${alerts.size} items")

        observeAlerts(totalAlertsCountText)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AlertsFragment", "Fragment view created")
    }

    private fun observeAlerts(totalAlertsCountText: TextView) {
        alertListener?.let { database.reference.child("/alerts").removeEventListener(it) }
        alertListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AlertsFragment", "Data received: ${snapshot.value}")
                val totalCount = snapshot.children.count()
                val activeCount = snapshot.children.count { it.child("status").value == "active" }
                handler.post {
                    totalAlertsCountText.text = "Tổng số cảnh báo: $totalCount (Đang hoạt động: $activeCount)"
                }
                Log.d("AlertsFragment", "Total count: $totalCount, Active count: $activeCount")

                val alertList = mutableListOf<Alert>()
                for (child in snapshot.children) {
                    val deviceId = child.child("deviceId").getValue(String::class.java) ?: continue
                    val message = child.child("message").getValue(String::class.java) ?: continue
                    val status = child.child("status").getValue(String::class.java) ?: continue
                    val timestamp = child.child("timestamp").getValue(String::class.java) ?: continue
                    val alert = Alert(deviceId, message, status, timestamp).apply { id = child.key ?: "" }
                    alertList.add(alert)
                    Log.d("AlertsFragment", "Manually mapped alert: $alert")
                }
                alertList.sortByDescending { it.timestamp }
                Log.d("AlertsFragment", "AlertList size after mapping: ${alertList.size}")

                // Cập nhật trong thread UI
                handler.post {
                    alerts.clear()
                    Log.d("AlertsFragment", "Alerts cleared, size: ${alerts.size}")
                    alerts.addAll(alertList)
                    Log.d("AlertsFragment", "Alerts updated, size: ${alerts.size}")
                    alertAdapter.updateAlerts(alerts.toList())
                    // Nếu DiffUtil không hoạt động, thử gọi notifyDataSetChanged
                    if (alerts.isNotEmpty() && alertAdapter.itemCount == 0) {
                        Log.d("AlertsFragment", "Forcing notifyDataSetChanged due to empty display")
                        alertAdapter.notifyDataSetChanged()
                    }
                    Log.d("AlertsFragment", "Adapter updated with ${alerts.size} items")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AlertsFragment", "Lỗi khi tải cảnh báo: ${error.message}")
            }
        }
        database.reference.child("/alerts").addValueEventListener(alertListener!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertListener?.let { database.reference.child("/alerts").removeEventListener(it) }
    }
}