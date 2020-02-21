package com.example.dreadmane.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dreadmane.R
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.fragments.ChapterFragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_rdv.*

class AddRdvActivity : AppCompatActivity(), View.OnClickListener{

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val dateSb = StringBuilder()
    private var dateCalendarDay: String? = null
    private var dateCalendarMonth: String? = null
    private var dateCalendarYear: String? = null
    private var rdvs : ArrayList<RdvData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rdv)
        setSupportActionBar(toolbar)

        readRdvDatabase()

        button_add_rdv_admin.setOnClickListener(this)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            dateCalendarDay = dayOfMonth.toString()
            dateCalendarMonth = (month + 1).toString()
            dateCalendarYear = year.toString()
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun valideAddRDV(){
        // val regexDate = Regex("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$")
        val regexHeure = Regex("^([0-1][0-9]|(2)[0-3])(\\:)([0-5][0-9])\$")
        if (regexHeure.containsMatchIn(editTextHeure.text) && dateCalendarDay != null){
            Toast.makeText(applicationContext,"C'est bon", Toast.LENGTH_SHORT).show()

            dateSb.clear()
            val date = dateSb.append(dateCalendarDay).append("/").append(dateCalendarMonth).append("/").append(dateCalendarYear).toString()
            database.child("rdv").child(rdvs.size.toString()).setValue(RdvData(date, editTextHeure.text.toString()))

        }
        else Toast.makeText(applicationContext, "Date ou Heure non inscrite", Toast.LENGTH_SHORT).show()

    }

    private  fun readRdvDatabase(){

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue(RdvData::class.java)
                if (comment != null) {
                    rdvs.add(comment)
                }

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ChapterFragment.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ChapterFragment.TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(applicationContext, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.child("rdv").addChildEventListener(childEventListener)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv_admin -> valideAddRDV()
        }
    }

}
