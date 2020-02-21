package com.example.dreadmane.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class RdvData(var date : String? = null, var heure: String?= null, var client : String? = null) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "date" to date,
            "heure" to heure,
            "client" to client

        )
    }
}