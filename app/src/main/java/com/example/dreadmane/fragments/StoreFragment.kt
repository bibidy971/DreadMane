package com.example.dreadmane.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.OnItemClickListenerProfil
import com.example.dreadmane.R
import com.example.dreadmane.RdvAdapter
import com.example.dreadmane.RdvAdapterProfil
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.viewModel.MyUserViewModel
import com.example.dreadmane.viewModel.RdvListViewModel
import com.squareup.picasso.Picasso


class StoreFragment : Fragment(), View.OnClickListener, OnItemClickListenerProfil{

    private lateinit var mCallBack : MyFragmentCallBack
    private lateinit var vEmailUser: TextView
    private lateinit var vNameUse : TextView
    private lateinit var vPictureUser : ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.createCallBack()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val result = inflater!!.inflate(R.layout.profile_utilisateur, container, false)

        result.findViewById<Button>(R.id.sign_out).setOnClickListener(this)
        vEmailUser = result.findViewById(R.id.profile_email)
        vNameUse = result.findViewById(R.id.profile_text)
        vPictureUser = result.findViewById(R.id.profile_image)

        recyclerView = result.findViewById(R.id.planing_rdv_list_profil) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context,1)

        return result
    }

    private fun changeView(listView : ArrayList<RdvData>){
        val newList = listView.filter { s -> s.client == user.uid } as ArrayList<RdvData>

        if (newList.isEmpty()){
            recyclerView.visibility = View.GONE
        }else{
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = context?.let { RdvAdapterProfil(newList,it,this) }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model : MyUserViewModel by viewModels()
        model.getUsers().observe(this, Observer { user ->
            this.user = user
            vNameUse.text = user.username
            vEmailUser.text = user.email
            Picasso.get().load(user.uriPhoto).centerInside().fit().into(vPictureUser)
        })

        val modelRdv : RdvListViewModel by viewModels()
        modelRdv.getRdv().observe(this, Observer { rdvsList ->
            changeView(rdvsList as ArrayList<RdvData>)
        })

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
            R.id.sign_out -> {
                mCallBack.disconnection()
            }
        }
    }

    interface MyFragmentCallBack{
        fun disconnection()
        fun infoRdvProfil(rdvData: RdvData)
    }

    override fun onItemClicked(rdvData: RdvData) {
        mCallBack.infoRdvProfil(rdvData)
    }

}




