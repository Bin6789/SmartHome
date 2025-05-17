package com.example.smarthome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeviceViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")
    private val _deviceList = MutableLiveData<List<Device>>()
    private val _deviceData = MutableLiveData<DeviceData>()
    private val _alertCount = MutableLiveData<Int>()
    private val _historyData = MutableLiveData<List<HistoryEntry>>()

    val deviceList: LiveData<List<Device>> get() = _deviceList
    val deviceData: LiveData<DeviceData> get() = _deviceData
    val alertCount: LiveData<Int> get() = _alertCount
    val historyData: LiveData<List<HistoryEntry>> get() = _historyData

    fun observeDevices() {
        database.reference.child("SmartHomeApp/devices")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val devices = snapshot.children.mapNotNull {
                        it.getValue(Device::class.java)?.copy(id = it.key ?: "")
                    }
                    _deviceList.value = devices
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun observeDevice(deviceId: String) {
        database.reference.child("SmartHomeApp/devices/$deviceId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(DeviceData::class.java) ?: DeviceData()
                    _deviceData.value = data
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun observeAlerts() {
        database.reference.child("SmartHomeApp/alerts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.children.count { it.child("status").value == "active" }
                    _alertCount.value = count
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun observeHistory(deviceId: String) {
        database.reference.child("SmartHomeApp/history/$deviceId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val history = snapshot.children.mapNotNull {
                        val action = it.child("action").getValue(String::class.java) ?: ""
                        val state = it.child("state").getValue(Boolean::class.java) ?: false
                        val timestamp = it.child("timestamp").getValue(String::class.java) ?: ""
                        HistoryEntry(action, state, timestamp)
                    }.sortedByDescending { it.timestamp.toLongOrNull() ?: 0L }
                    _historyData.value = history
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}