package com.example.smarthome

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeviceDetailsFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")
    private var valueEventListener: ValueEventListener? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Tải layout cho Fragment
        return inflater.inflate(R.layout.fragment_device_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy deviceId từ arguments
        val deviceId = arguments?.getString("deviceId") ?: "DEV001"

        // Khởi tạo các view
        val deviceName = view.findViewById<TextView>(R.id.device_name)
        val temperatureText = view.findViewById<TextView>(R.id.temperature)
        val humidityText = view.findViewById<TextView>(R.id.humidity)
        val relaySwitch = view.findViewById<Switch>(R.id.relay_switch)
        val ledSwitch = view.findViewById<Switch>(R.id.led_switch)
        val chart = view.findViewById<LineChart>(R.id.chart)

        // Hiển thị tên thiết bị
        deviceName.text = "Chi tiết thiết bị: $deviceId"

        // Lắng nghe dữ liệu từ Firebase
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(DeviceData::class.java) ?: DeviceData()
                temperatureText.text = "Nhiệt độ: ${data.temperature}°C"
                humidityText.text = "Độ ẩm: ${data.humidity}%"
                relaySwitch.isChecked = data.relay
                ledSwitch.isChecked = data.led

                // Cập nhật biểu đồ (giả lập dữ liệu)
                val entries = listOf(Entry(1f, data.temperature), Entry(2f, data.humidity))
                val dataSet = LineDataSet(entries, "Dữ liệu cảm biến")
                chart.data = LineData(dataSet)
                chart.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần (ví dụ: hiển thị thông báo)
            }
        }

        // Thêm listener cho Firebase
        database.reference.child("/devices/$deviceId")
            .addValueEventListener(valueEventListener!!)

        // Xử lý sự kiện thay đổi trạng thái Switch
        relaySwitch.setOnCheckedChangeListener { _, isChecked ->
            database.reference.child("/devices/$deviceId/relay").setValue(isChecked)
        }

        ledSwitch.setOnCheckedChangeListener { _, isChecked ->
            database.reference.child("/devices/$deviceId/led").setValue(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Gỡ bỏ listener để tránh rò rỉ bộ nhớ
        valueEventListener?.let {
            database.reference.child("/devices").removeEventListener(it)
        }
    }
}