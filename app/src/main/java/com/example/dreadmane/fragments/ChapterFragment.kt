package com.example.dreadmane.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.R
import com.example.dreadmane.RdvAdapter
import com.example.dreadmane.data.RdvData
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
        recyclerView.adapter = context?.let { RdvAdapter(rdvs, it) }

        result.findViewById<Button>(R.id.button_add_rdv).setOnClickListener(this)


        database.child("rdv").setValue(rdvs)

        return result
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.adapter = context?.let { RdvAdapter(rdvs,it) }

    }

    private fun initListofString(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.value as ArrayList<RdvData>
                rdvs = post
                rdvs.removeAt(1)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        database.child("rdv").addValueEventListener(postListener)

        for (i in 0..10){
            rdvs.add(RdvData("17/0${i+1}/1971", "${i}h70", null))
        }
    }

    private fun createCallBack(){
        try { //Parent activity will automatically subscribe to callback
            mCallBack = (activity as MyFragmentCallBack)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("$e must implement OnButtonClickedListener")
        }
    }

    private fun checkAdminUser(){

    }

    interface  MyFragmentCallBack{
        var userId : String?
        fun addRdvButton()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv -> mCallBack.addRdvButton()
        }
    }


}