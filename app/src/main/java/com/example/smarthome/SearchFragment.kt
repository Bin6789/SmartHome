package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    private lateinit var deviceAdapter: DeviceAdapter
    private val devices = mutableListOf<Device>()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.search_results)
        recyclerView.layoutManager = LinearLayoutManager(context)
        deviceAdapter = DeviceAdapter(devices) { device ->
            val intent = Intent(context, DeviceDetailsActivity::class.java)
            intent.putExtra("deviceId", device.id)
            startActivity(intent)
        }
        recyclerView.adapter = deviceAdapter

        val searchInput = view.findViewById<EditText>(R.id.search_input)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                database.reference.child("SmartHomeApp/devices")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val filteredDevices = snapshot.children.mapNotNull {
                                val device = it.getValue(Device::class.java)?.copy(id = it.key ?: "")
                                if (device?.name?.lowercase()?.contains(query) == true ||
                                    device?.id?.lowercase()?.contains(query) == true) {
                                    device
                                } else {
                                    null
                                }
                            }
                            deviceAdapter.updateDevices(filteredDevices)
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }
}