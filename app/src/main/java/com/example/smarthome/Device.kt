package com.example.smarthome

data class Device(
    val id: String,
    val name: String,
    val status: String
) {
    // Firebase yêu cầu constructor không tham số
    constructor() : this("", "","")
}
