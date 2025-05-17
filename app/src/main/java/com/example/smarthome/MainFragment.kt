package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    private val viewModel: DeviceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        observeData(view)

        view.findViewById<androidx.cardview.widget.CardView>(R.id.devices_card).setOnClickListener {
            val deviceId = "DEV001"
            val bundle = Bundle().apply { putString("deviceId", deviceId) }
            findNavController().navigate(R.id.action_mainFragment_to_devicesFragment, bundle)
        }

        view.findViewById<androidx.cardview.widget.CardView>(R.id.alerts_card).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_alertsFragment)
        }

        view.findViewById<androidx.cardview.widget.CardView>(R.id.history_card).setOnClickListener {
            val deviceId = "DEV001"
            val bundle = Bundle().apply { putString("deviceId", deviceId) }
            findNavController().navigate(R.id.action_mainFragment_to_historyFragment, bundle)
        }

        return view
    }

    private fun observeData(view: View) {
        viewModel.observeDevices()
        viewModel.deviceList.observe(viewLifecycleOwner) { devices ->
            val activeDevices = devices.count { it.status == "online" }
            view.findViewById<TextView>(R.id.devices_count).text = devices.size.toString()
            view.findViewById<TextView>(R.id.devices_status).text = "ACTIVE $activeDevices"
        }

        viewModel.observeAlerts()
        viewModel.alertCount.observe(viewLifecycleOwner) { count ->
            view.findViewById<TextView>(R.id.alerts_count).text = count.toString()
            view.findViewById<TextView>(R.id.alerts_status).text = "ACTIVE $count"
        }
        val deviceId = "DEV001"
        viewModel.observeHistory(deviceId)
        viewModel.historyData.observe(viewLifecycleOwner) { count ->
            view.findViewById<TextView>(R.id.history_count).text = count.toString()
            view.findViewById<TextView>(R.id.history_status).text = "RECORDED $count"
        }
    }
}