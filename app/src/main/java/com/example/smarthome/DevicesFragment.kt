package com.example.smarthome

import android.content.Intent
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

class DevicesFragment : Fragment() {

    private lateinit var deviceAdapter: DeviceAdapter
    private val devices = mutableListOf<Device>()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_devices, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.devices_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        deviceAdapter = DeviceAdapter(devices) { device ->
            val intent = Intent(context, DeviceDetailsActivity::class.java)
            intent.putExtra("deviceId", device.id)
            startActivity(intent)
        }
        recyclerView.adapter = deviceAdapter

        database.reference.child("SmartHomeApp/devices")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newDevices = snapshot.children.mapNotNull {
                        it.getValue(Device::class.java)?.copy(id = it.key ?: "")
                    }
                    deviceAdapter.updateDevices(newDevices)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return view
    }
}