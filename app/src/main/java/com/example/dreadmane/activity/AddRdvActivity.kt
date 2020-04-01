package com.example.dreadmane.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.CalendarView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.dreadmane.R
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.viewModel.RdvListViewModel
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_rdv.*
import java.text.SimpleDateFormat

class AddRdvActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val PERMISSION_ID = 42
    }

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var dateCalendarDay: String? = null
    private var dateCalendarMonth: String? = null
    private var dateCalendarYear: String? = null
    private lateinit var date : String
    private var rdvs : ArrayList<RdvData> = ArrayList()
    private lateinit var timePicker: TimePicker
    private lateinit var calendarView : CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rdv)
        setSupportActionBar(toolbar)

        button_add_rdv_admin.setOnClickListener(this)

        timePicker = findViewById(R.id.timePicker1)
        timePicker.setIs24HourView(true)
        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            dateCalendarDay = addZero(dayOfMonth)
            dateCalendarMonth = addZero(month + 1)
            dateCalendarYear = year.toString()
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        val modelRdv : RdvListViewModel by viewModels()
        modelRdv.getRdv().observe(this, Observer { rdvsList ->
            rdvs = rdvsList as ArrayList<RdvData>
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun valideAddRDV(){
        // val regexDate = Regex("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$")
        // val regexHeure = Regex("^([0-1][0-9]|(2)[0-3])(\\:)([0-5][0-9])\$")
        if (calendarView.visibility != View.GONE) {

            if (dateCalendarDay != null) {
                val dateSb = StringBuilder()
                date = dateSb.append(dateCalendarDay).append("/").append(dateCalendarMonth).append("/").append(dateCalendarYear).toString()
                calendarView.visibility = View.GONE
                timePicker.visibility = View.VISIBLE

            } else Toast.makeText(applicationContext, "Selectione une date", Toast.LENGTH_SHORT).show()

        }else if (timePicker.visibility != View.GONE){

            val dateSb = StringBuilder()
            val time = dateSb.append(addZero(timePicker.hour)).append(":").append(addZero(timePicker.minute)).toString()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ajout RDV")
            builder.setMessage("Voulez vous ajouté se RDV ?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                //TODO : Verifier connexion
                database.child("rdv").setValue(addDateToListSorted(RdvData(date = date, heure = time)))
                Toast.makeText(applicationContext, "C'est bon", Toast.LENGTH_SHORT).show()

                calendarView.visibility = View.VISIBLE
                timePicker.visibility = View.GONE

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dateCalendarDay == null

                calendarView.visibility = View.VISIBLE
                timePicker.visibility = View.GONE
            }
            builder.show()

        }

    }

    private fun addZero(int: Int) : String{
        val sb = StringBuilder()
        return if (int == 0) sb.append("00").toString() else if (int < 10) sb.append("0").append(int).toString() else sb.append(int).toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun addDateToListSorted(rdvData: RdvData): List<RdvData> {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val list = rdvs
        list.add(rdvData)
        return list.sortedWith(compareBy( {format.parse(it.date)}, {it.heure}))
    }

    @SuppressLint("SimpleDateFormat")
    private fun triRdv(): List<RdvData> {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return rdvs.sortedWith(compareBy( {format.parse(it.date)}, {it.heure}))
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv_admin -> valideAddRDV()
        }
    }

}
