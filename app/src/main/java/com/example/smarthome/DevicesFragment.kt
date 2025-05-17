package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider

class DevicesFragment : Fragment() {

    private lateinit var deviceAdapter: DeviceAdapter
    private val devices = mutableListOf<Device>()
    private lateinit var deviceViewModel: DeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_devices, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.devices_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        deviceAdapter = DeviceAdapter(devices) { device ->
            val action = DevicesFragmentDirections.actionDevicesFragmentToDeviceDetailsFragment(device.id)
            findNavController().navigate(action)
        }
        recyclerView.adapter = deviceAdapter

        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceViewModel.deviceList.observe(viewLifecycleOwner) { devices ->
            deviceAdapter.updateDevices(devices)
        }
        deviceViewModel.observeDevices() // Gọi phương thức trong ViewModel
    }
}