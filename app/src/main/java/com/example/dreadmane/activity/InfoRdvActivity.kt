package com.example.dreadmane.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.dreadmane.R
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.viewModel.MyUserViewModel
import com.example.dreadmane.viewModel.RdvListViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_info_rdv.*

class InfoRdvActivity : AppCompatActivity(), View.OnClickListener {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var user: User
    private lateinit var rdvData: RdvData
    private var rdvlist : ArrayList<RdvData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_rdv)

        rdvData = intent.extras.get("rdv") as RdvData

        textView2.text = rdvData.heure
        textView.text = rdvData.date

        button_supp.setOnClickListener(this)
        button_prendre_rdv.setOnClickListener(this)
        button_annule_rdv.setOnClickListener(this)

        button_annule_rdv.visibility = if (rdvData.client != null) View.VISIBLE else View.GONE
        button_prendre_rdv.visibility = if (rdvData.client != null) View.GONE else View.VISIBLE

        val model : MyUserViewModel by viewModels()
        model.getUsers().observe(this, Observer<User>{ user ->
            this.user = user
            button_supp.visibility = if(user.admin) View.VISIBLE else View.GONE
        })

        val modelRdv : RdvListViewModel by viewModels()
        modelRdv.getRdv().observe(this, Observer<List<RdvData>> { rdvsList ->
            rdvlist = rdvsList as ArrayList<RdvData>
        })
    }

    private fun suppRdv(){
        if (rdvlist.remove(rdvData)){
            database.child("rdv").setValue(rdvlist)
            finish()
        }else{
            //TODO() : Rdv pas present
        }
    }

    private fun takeRdv(){
        if (rdvlist.contains(rdvData)){
            rdvlist[rdvlist.indexOf(rdvData)].client = user.uid
            database.child("rdv").setValue(rdvlist)
            finish()
        }
    }

    private fun annuleRdv(){
        if (rdvlist.contains(rdvData)){
            rdvlist[rdvlist.indexOf(rdvData)].client = null
            database.child("rdv").setValue(rdvlist)
            finish()
        }
    }

    override fun onClick(p0: View?) {
        when(p0){
            button_supp -> suppRdv()
            button_prendre_rdv -> takeRdv()
            button_annule_rdv -> annuleRdv()
        }
    }

}
