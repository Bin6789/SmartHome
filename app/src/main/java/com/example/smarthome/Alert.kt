package com.example.smarthome

data class Alert(
    val deviceId: String = "",
    val message: String = "",
    val status: String = "",
    val timestamp: String = ""
) {
    constructor() : this("", "", "", "")
    var id: String = "" // Để lưu key (ALERT001, v.v.)
}