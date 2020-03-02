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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.R
import com.example.dreadmane.RdvAdapter
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.viewModel.MyUserViewModel
import com.example.dreadmane.viewModel.RdvListViewModel
import com.google.firebase.database.*


class ChapterFragment : Fragment(), View.OnClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var mCallBack : MyFragmentCallBack
    private lateinit var mButtonAddRdv : Button
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

        recyclerView = result.findViewById(R.id.planing_rdv_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context,1)

        result.findViewById<Button>(R.id.button_add_rdv).setOnClickListener(this)

        mButtonAddRdv = result.findViewById(R.id.button_add_rdv)

        return result
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model : MyUserViewModel by viewModels()
        model.getUsers().observe(this, Observer<User>{ user ->
            mButtonAddRdv.visibility = if(user.admin) View.VISIBLE else View.GONE
        })

        val modelRdv : RdvListViewModel by viewModels()
        modelRdv.getRdv().observe(this, Observer<List<RdvData>> { rdvsList ->
            changeView(rdvsList as ArrayList<RdvData>)
        })

    }

    override fun onStart() {
        super.onStart()


    }

    private fun changeView(listView : ArrayList<RdvData>){
        recyclerView.adapter = context?.let { RdvAdapter(listView,it) }
    }


    private fun createCallBack(){
        try { //Parent activity will automatically subscribe to callback
            mCallBack = (activity as MyFragmentCallBack)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("$e must implement OnButtonClickedListener")
        }
    }

    interface  MyFragmentCallBack{
        fun addRdvButton()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv -> mCallBack.addRdvButton()
        }
    }


}