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
import android.util.Log
import android.widget.TextView

class AlertsFragment : Fragment() {

    private lateinit var alertAdapter: AlertAdapter
    private val alerts = mutableListOf<Alert>()
    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")
    private var alertListener: ValueEventListener? = null

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
                val totalCount = snapshot.children.count() // Tổng số alerts
                val activeCount = snapshot.children.count { it.child("status").value == "active" }
                totalAlertsCountText.text = "Tổng số cảnh báo: $totalCount (Đang hoạt động: $activeCount)"
                Log.d("AlertsFragment", "Total count: $totalCount, Active count: $activeCount")

                val alertList = snapshot.children.mapNotNull { child ->
                    val alert = child.getValue(Alert::class.java)
                    alert?.apply { id = child.key ?: "" }
                    Log.d("AlertsFragment", "Mapped alert: $alert")
                    alert
                }.sortedByDescending { it.timestamp }
                alerts.clear()
                alerts.addAll(alertList)
                alertAdapter.updateAlerts(alerts)
                Log.d("AlertsFragment", "Alerts list size: ${alerts.size}")
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