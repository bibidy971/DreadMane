package com.example.dreadmane

import android.util.Log
import com.example.dreadmane.dto.MultiplePushDTO
import com.example.dreadmane.dto.PushDTO
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class FcmPush {
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AIzaSyApGtpD91FJbNLjUklU0MInGLiK6wr8yLA"
    var gson : Gson? = null
    var okHttpClient : OkHttpClient ? = null
    companion object{
        var instance = FcmPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid : String, title : String, message : String){
        val pushDTO = PushDTO().apply { to = destinationUid
            notification.title = title
            notification.body = message }


        val body = gson?.toJson(pushDTO)!!
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","key=${serverKey}")
            .url(url)
            .post(body)
            .build()

        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("SEND","ECHEC")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("SEND", response.message)
            }

        })
    }
    fun multipleSendMessage(to : List<String>, title: String, message: String){
        val pushDTO = MultiplePushDTO().apply { registration_ids=to
        notification.body=message
        notification.title=title}

        val body = gson?.toJson(pushDTO)!!
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","key=${serverKey}")
            .url(url)
            .post(body)
            .build()

        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("SEND","ECHEC")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("SEND", response.message)
            }

        })
    }

}