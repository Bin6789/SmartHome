package com.example.smarthome

data class DeviceData(
    val temperature: Float = 0f,
    val humidity: Float = 0f,
    val relay: Boolean = false,
    val led: Boolean = false
)
