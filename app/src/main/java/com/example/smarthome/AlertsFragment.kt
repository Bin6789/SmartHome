package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AlertsFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alerts, container, false)
        val alertsCountText = view.findViewById<TextView>(R.id.alerts_count)

        database.reference.child("SmartHomeApp/alerts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.children.count { it.child("status").value == "active" }
                    alertsCountText.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return view
    }
}