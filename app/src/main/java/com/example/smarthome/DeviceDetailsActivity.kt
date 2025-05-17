package com.example.smarthome

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeviceDetailsActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_device_details)

        val deviceId = intent.getStringExtra("deviceId") ?: "DEV001"
        val deviceName = findViewById<TextView>(R.id.device_name)
        val temperatureText = findViewById<TextView>(R.id.temperature)
        val humidityText = findViewById<TextView>(R.id.humidity)
        val relaySwitch = findViewById<Switch>(R.id.relay_switch)
        val ledSwitch = findViewById<Switch>(R.id.led_switch)
        val chart = findViewById<LineChart>(R.id.chart)

        deviceName.text = "Device Details: $deviceId"

        database.reference.child("SmartHomeApp/devices/$deviceId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(DeviceData::class.java) ?: DeviceData()
                    temperatureText.text = "Temperature: ${data.temperature}°C"
                    humidityText.text = "Humidity: ${data.humidity}%"
                    relaySwitch.isChecked = data.relay
                    ledSwitch.isChecked = data.led

                    // Cập nhật biểu đồ (giả lập dữ liệu)
                    val entries = listOf(Entry(1f, data.temperature), Entry(2f, data.humidity))
                    val dataSet = LineDataSet(entries, "Sensor Data")
                    chart.data = LineData(dataSet)
                    chart.invalidate()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        relaySwitch.setOnCheckedChangeListener { _, isChecked ->
            database.reference.child("SmartHomeApp/devices/$deviceId/relay").setValue(isChecked)
        }

        ledSwitch.setOnCheckedChangeListener { _, isChecked ->
            database.reference.child("SmartHomeApp/devices/$deviceId/led").setValue(isChecked)
        }
    }
}