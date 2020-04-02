package com.example.dreadmane.activity

import android.os.Bundle
import android.os.MessageQueue
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.dreadmane.FcmPush
import com.example.dreadmane.R
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.viewModel.MyUserViewModel
import com.example.dreadmane.viewModel.RdvListViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
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
        //FcmPush.instance.sendMessage("/topics/weather","Test topic","DreadMane")
        val list = listOf<String>("cD5uMl5cQfSCpbiRMGcitT:APA91bGZ805Ydd0A5x_zBTIBC5AlHorDslGWK3Zqcnbzu_dKELnY5A-ragamnfvX_1D1px_MHbevZKT6xlfLKfIfVa27jymLwWQKzit-UJuQR-StexoTmyOzaMzprWKlcxG0zgzC2sVn","f7Oo3MkSQ1Gu5rIkiPyhy1:APA91bHS4VkhVlqwZ1cjCVC4sKk0Nqdwea0H99RXye6mSjJGsxGKEcC2tyrrDDUNNMUiTzfWJ5DWeAbjA4aZ-wqYaVECqMiata4xI3YEPRmnBU4un6iDWYd4IstOXMXsYBw39MQ4SxZu")
        //FcmPush.instance.multipleSendMessage(list,"Multiple","Test envoie multiple")
    }


    private fun annuleRdv(){
        FcmPush.instance.sendMessage("/topics/admin","Test topic","DreadMane")
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
