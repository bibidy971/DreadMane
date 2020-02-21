package com.example.dreadmane.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dreadmane.R
import com.example.dreadmane.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class StoreFragment : Fragment(), View.OnClickListener{

    private lateinit var mCallBack : MyFragmentCallBack
    private lateinit var vEmailUser: TextView
    private lateinit var vNameUse : TextView
    private val database = FirebaseDatabase.getInstance().reference

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.createCallBack()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val authUser = FirebaseAuth.getInstance().currentUser!!

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val post = p0.getValue(User::class.java)

            }
        }
        database.child("users").child(authUser.uid).addListenerForSingleValueEvent(postListener)

        val result = inflater!!.inflate(R.layout.profile_utilisateur, container, false)

        result.findViewById<Button>(R.id.sign_out).setOnClickListener(this)
        vEmailUser = result.findViewById(R.id.profile_email)
        vNameUse = result.findViewById(R.id.profile_text)
        Picasso.get().load(mCallBack.photoUser).centerInside().fit().into(result.findViewById(R.id.profile_image) as ImageView)

        return result
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vNameUse.text = mCallBack.nameUser
        vEmailUser.text = mCallBack.eMailUser

    }

    private fun createCallBack(){
        try { //Parent activity will automatically subscribe to callback
            mCallBack = (activity as MyFragmentCallBack)!!
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
        var eMailUser : String?
        var nameUser : String?
        var photoUser : Uri?
        fun disconnection()
    }

}




