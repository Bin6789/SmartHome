package com.example.smarthome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

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

    private var deviceListener: ValueEventListener? = null
    private var deviceDataListener: ValueEventListener? = null
    private var alertListener: ValueEventListener? = null
    private var historyListener: ValueEventListener? = null

    fun observeDevices() {
        deviceListener?.let { database.reference.child("/devices").removeEventListener(it) }
        deviceListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val devices = snapshot.children.mapNotNull {
                    it.getValue(Device::class.java)?.copy(id = it.key ?: "")
                }
                _deviceList.value = devices
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DeviceViewModel", "Lỗi khi tải danh sách thiết bị: ${error.message}")
            }
        }
        database.reference.child("/devices").addValueEventListener(deviceListener!!)
    }

    fun observeDevice(deviceId: String) {
        deviceDataListener?.let { database.reference.child("/devices/$deviceId").removeEventListener(it) }
        deviceDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(DeviceData::class.java) ?: DeviceData()
                _deviceData.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DeviceViewModel", "Lỗi khi tải dữ liệu thiết bị: ${error.message}")
            }
        }
        database.reference.child("/devices/$deviceId").addValueEventListener(deviceDataListener!!)
    }

    fun observeAlerts() {
        alertListener?.let { database.reference.child("/alerts").removeEventListener(it) }
        alertListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.children.count { it.child("status").value == "active" }
                _alertCount.value = count
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DeviceViewModel", "Lỗi khi tải cảnh báo: ${error.message}")
            }
        }
        database.reference.child("/alerts").addValueEventListener(alertListener!!)
    }

    fun observeHistory(deviceId: String) {
        historyListener?.let { database.reference.child("/history/$deviceId").removeEventListener(it) }
        historyListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val history = snapshot.children.mapNotNull {
                    val action = it.child("action").getValue(String::class.java) ?: ""
                    val state = it.child("state").getValue(Boolean::class.java)
                    val timestamp = it.child("timestamp").getValue(String::class.java) ?: ""
                    val value = it.child("value").getValue(Double::class.java)
                    HistoryEntry(action, state, timestamp, value)
                }.sortedByDescending {
                    try {
                        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                        isoFormat.parse(it.timestamp)?.time ?: 0L
                    } catch (e: Exception) {
                        try {
                            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                            isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                            isoFormat.parse(it.timestamp)?.time ?: 0L
                        } catch (e: Exception) {
                            0L // Mặc định nếu không parse được
                        }
                    }
                }
                _historyData.value = history
                Log.d("DeviceViewModel", "Loaded ${history.size} history entries for device $deviceId")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DeviceViewModel", "Lỗi khi tải lịch sử: ${error.message}")
            }
        }
        database.reference.child("/history/$deviceId").addValueEventListener(historyListener!!)
    }

    override fun onCleared() {
        super.onCleared()
        deviceListener?.let { database.reference.child("/devices").removeEventListener(it) }
        deviceDataListener?.let { database.reference.child("/devices").removeEventListener(it) }
        alertListener?.let { database.reference.child("/alerts").removeEventListener(it) }
        historyListener?.let { database.reference.child("/history").removeEventListener(it) }
    }
}