package com.example.dreadmane.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (var username: String? = null, var email: String? = null, var uriPhoto : String? = null, var admin : Boolean = false)
