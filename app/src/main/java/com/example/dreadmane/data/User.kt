package com.example.dreadmane.data

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User (var username: String? = null,
                 var uid: String = "",
                 var email: String? = null,
                 var uriPhoto : String? = null,
                 var admin : Boolean = false,
                 var tokenFirebaseMessage : String? = null) : Serializable
