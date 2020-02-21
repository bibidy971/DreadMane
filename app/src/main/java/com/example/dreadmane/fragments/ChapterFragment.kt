package com.example.dreadmane.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.R
import com.example.dreadmane.RdvAdapter
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.google.firebase.database.*


class ChapterFragment : Fragment(), View.OnClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var mCallBack : MyFragmentCallBack
    private var rdvs : ArrayList<RdvData> = ArrayList()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object{
        const val TAG = "ChatpterFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.createCallBack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val result = inflater.inflate(R.layout.planing_list, container, false)

        initListofString()

        recyclerView = result.findViewById(R.id.planing_rdv_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context,1)

        result.findViewById<Button>(R.id.button_add_rdv).setOnClickListener(this)

        if (mCallBack.userInfo.admin){
            val bouton = result.findViewById<Button>(R.id.button_add_rdv)
            bouton.visibility = View.VISIBLE
        }


        //database.child("rdv").setValue(rdvs)

        return result
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.adapter = context?.let { RdvAdapter(rdvs,it) }

    }

    override fun onStart() {
        super.onStart()


    }

    private fun changeView(listView : ArrayList<RdvData>){
        recyclerView.adapter = context?.let { RdvAdapter(listView,it) }
    }

    private fun initListofString(){

        /*val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.value as ArrayList<RdvData>
                changeView(post)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        database.child("rdv").addListenerForSingleValueEvent(postListener)*/

        /*for (i in 0..10){
            rdvs.add(RdvData("17/0${i+1}/1971", "${i}h70", null))
        }*/


        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue(RdvData::class.java)
                if (comment != null) {
                    rdvs.add(comment)
                }
                changeView(rdvs)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

                rdvs[commentKey?.toInt()!!] = newComment!!
                changeView(rdvs)

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                rdvs.remove(dataSnapshot.getValue(RdvData::class.java))
                changeView(rdvs)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.child("rdv").addChildEventListener(childEventListener)
    }

    private fun createCallBack(){
        try { //Parent activity will automatically subscribe to callback
            mCallBack = (activity as MyFragmentCallBack)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("$e must implement OnButtonClickedListener")
        }
    }

    private fun checkAdminUser(){

        database.child("rdv").child(rdvs.size.toString()).setValue(RdvData("11/11/1995","11:11"))
    }

    interface  MyFragmentCallBack{
        var userId : String?
        var userInfo : User
        fun addRdvButton()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv -> mCallBack.addRdvButton()
        }
    }


}