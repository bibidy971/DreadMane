package com.example.dreadmane.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.example.dreadmane.FcmPush
import com.example.dreadmane.R
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.viewModel.MyUserViewModel
import com.example.dreadmane.viewModel.RdvListViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_info_rdv.*


class InfoRdvActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val TAG = "InfoRDV"
        const val CHANNEL_ID = "com.example.dreadmane.activity"
    }

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var user: User
    private lateinit var rdvData: RdvData
    private var rdvlist : ArrayList<RdvData> = arrayListOf()
    private val modelRdv : RdvListViewModel by viewModels()
    private val model : MyUserViewModel by viewModels()

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.dreadmane.activity"
    private val description = "Test notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_rdv)

        rdvData = intent.extras?.get("rdv") as RdvData

        textView2.text = rdvData.heure
        textView.text = rdvData.date

        button_supp.setOnClickListener(this)
        button_prendre_rdv.setOnClickListener(this)
        button_annule_rdv.setOnClickListener(this)

        button_annule_rdv.visibility = if (rdvData.client != null) View.VISIBLE else View.GONE
        button_prendre_rdv.visibility = if (rdvData.client != null) View.GONE else View.VISIBLE


        model.getUsers().observe(this, Observer { user ->
            this.user = user
            button_supp.visibility = if(user.admin) View.VISIBLE else View.GONE
        })

        modelRdv.getRdv().observe(this, Observer { rdvsList ->
            rdvlist = rdvsList as ArrayList<RdvData>
        })

    }

    private fun suppRdv(){
        modelRdv.deleteRDV(rdvData)
        finish()
    }

    private fun takeRdv(){

        sendNotification()

        modelRdv.takeRdv(rdvData,user.uid)
        finish()
    }


    private fun sendNotification() {
        FcmPush.instance.sendMessage("dtutGai0SWY:APA91bEKF4N7au-hSmtAP6xFs1VPX4wfDuhL9v6BYJPiUPm6Y9f33e2BNldVaXZ5ehhJQvRvAUzPyjhUAleusOABdr7IJ51teU7GLZfwiJW_10CM2ufz1a_bBrog6sc2yytsY5l3exl_","Test","DreadMane")
    }


    private fun annuleRdv(){
        modelRdv.annuleRdv(rdvData)
        finish()
    }

    override fun onClick(p0: View?) {
        when(p0){
            button_supp -> suppRdv()
            button_prendre_rdv -> takeRdv()
            button_annule_rdv -> annuleRdv()
        }
    }

}
