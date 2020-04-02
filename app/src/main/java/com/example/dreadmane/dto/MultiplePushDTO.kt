package com.example.dreadmane.dto

data class MultiplePushDTO(var notification: Notification = Notification(), var registration_ids : List<String>? = null) {
    data class Notification(var body : String? = null , var title: String? = null)
}