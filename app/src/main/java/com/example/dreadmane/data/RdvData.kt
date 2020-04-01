package com.example.dreadmane.data

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable


@IgnoreExtraProperties
data class RdvData(var date : String? = null,
                   var heure: String?= null,
                   var client : String? = null,
                   var confirme : Boolean = false): Serializable