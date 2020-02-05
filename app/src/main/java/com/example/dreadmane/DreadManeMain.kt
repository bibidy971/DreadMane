package com.example.dreadmane

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class DreadManeMain : AppCompatActivity() {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dread_mane_main)


    }

    companion object {
        const val GOOGLE_ACCOUNT = "google_account"
    }
}