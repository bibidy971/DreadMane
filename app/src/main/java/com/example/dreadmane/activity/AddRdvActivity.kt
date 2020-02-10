package com.example.dreadmane.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.dreadmane.R

import kotlinx.android.synthetic.main.activity_add_rdv.*
import java.util.*

class AddRdvActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rdv)
        setSupportActionBar(toolbar)

        button_add_rdv_admin.setOnClickListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun valideAddRDV(){
        val dateRDV : Long = Date.parse(editTextDate.text.toString())
        Toast.makeText(applicationContext, "Tien : ${editTextDate.text}", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv_admin -> valideAddRDV()
        }
    }

}
