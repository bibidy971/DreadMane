package com.example.dreadmane.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.OnItemClickListener
import com.example.dreadmane.R
import com.example.dreadmane.RdvAdapter
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.viewModel.MyUserViewModel
import com.example.dreadmane.viewModel.RdvListViewModel


class ChapterFragment : Fragment(), View.OnClickListener, OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mCallBack : MyFragmentCallBack
    private lateinit var mButtonAddRdv : Button
    private lateinit var mButtonDeleteRdv : Button
    private var user: User ? = null

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


        mButtonDeleteRdv = result.findViewById(R.id.button_delete_rdv)
        mButtonAddRdv = result.findViewById(R.id.button_add_rdv)

        mButtonAddRdv.setOnClickListener(this)
        mButtonDeleteRdv.setOnClickListener(this)

        return result
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model : MyUserViewModel by viewModels()
        model.getUsers().observe(viewLifecycleOwner, Observer { user ->
            this.user = user
            mButtonAddRdv.visibility = if(user.admin) View.VISIBLE else View.GONE
        })

        val modelRdv : RdvListViewModel by viewModels()
        /*modelRdv.getRdv().observe(viewLifecycleOwner, Observer { rdvsList ->
            changeView(rdvsList as ArrayList<RdvData>)
        })*/

        modelRdv.getAllRdv().observe(viewLifecycleOwner, Observer { rdvsList ->
            changeView(ArrayList(rdvsList.values))
        })

    }

    private fun changeView(listView : ArrayList<RdvData>){
        if (user != null) {
            if (user?.admin!!) {
                recyclerView.adapter = context?.let { RdvAdapter(listView, it, this) }
            } else {
                val newList =
                    listView.filter { s -> s.client.isNullOrBlank() || s.client == user?.uid } as ArrayList<RdvData>
                recyclerView.adapter = context?.let { RdvAdapter(newList, it, this) }
            }
        }
    }


    private fun createCallBack(){
        try { //Parent activity will automatically subscribe to callback
            mCallBack = (activity as MyFragmentCallBack)
        } catch (e: ClassCastException) {
            throw ClassCastException("$e must implement OnButtonClickedListener")
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button_add_rdv -> mCallBack.addRdvButton()
        }
    }

    override fun onItemClicked(rdvData: RdvData) {
        mCallBack.infoRdv(rdvData)
    }

    interface  MyFragmentCallBack{
        fun addRdvButton()
        fun infoRdv(rdvData: RdvData)
    }

}