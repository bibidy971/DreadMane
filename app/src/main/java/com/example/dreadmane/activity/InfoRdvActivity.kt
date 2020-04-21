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
        private const val TOPIC_ADMIN = "/topics/admin"
        private const val TOPIC_USERS = "/topics/users"
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
            button_confirme.visibility = if (rdvData.client != null) View.VISIBLE else View.GONE
        })

        modelRdv.getAllRdv().observe(this, Observer { rdvslist ->
            rdvlist = rdvslist.values as ArrayList<RdvData>
        })

    }

    private fun suppRdv(){
        modelRdv.deleteRDV(rdvData)
        finish()
    }

    private fun takeRdv(){
        FcmPush.instance.sendMessage("/topics/admin","Prise de RDV","${user.username} vien de prendre le RDV du ${rdvData.date}")
        modelRdv.takeRdv(rdvData,user.uid)
        finish()
    }


    private fun annuleRdv(){

        val destinataire  = if (rdvData.confirme != null) rdvData.confirme!! else TOPIC_ADMIN
        FcmPush.instance.sendMessage(destinataire, "Annule RDV", "${user.username} vien d'annuler le RDV du ${rdvData.date}")

        modelRdv.annuleRdv(rdvData)
        finish()
    }
    private fun confirmeRdv(){

    }

    override fun onClick(p0: View?) {
        when(p0){
            button_supp -> suppRdv()
            button_prendre_rdv -> takeRdv()
            button_annule_rdv -> annuleRdv()
            button_confirme -> confirmeRdv()
        }
    }

}
