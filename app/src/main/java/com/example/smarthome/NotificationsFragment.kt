package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationsFragment : Fragment() {

    private lateinit var notificationAdapter: NotificationAdapter
    private val notifications = mutableListOf<NotificationItem>()
    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.notifications_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        notificationAdapter = NotificationAdapter(notifications)
        recyclerView.adapter = notificationAdapter

        if (auth.currentUser != null) {
            database.reference.child("/notifications/${auth.currentUser!!.uid}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        notifications.clear()
                        for (child in snapshot.children) {
                            val message = child.child("message").getValue(String::class.java) ?: ""
                            val timestamp = child.child("timestamp").getValue(String::class.java) ?: ""
                            notifications.add(NotificationItem(message, timestamp))
                        }
                        notificationAdapter.updateNotifications(notifications)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        return view
    }
}