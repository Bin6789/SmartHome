package com.example.smarthome

data class HistoryEntry(
    val action: String = "",
    val state: Boolean? = null, // Có thể null vì không phải mục nào cũng có state
    val timestamp: String = "",
    val value: Double? = null // Có thể null vì không phải mục nào cũng có value
) {
    constructor() : this("", null, "", null)
}