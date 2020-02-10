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
import com.squareup.picasso.Picasso


class StoreFragment : Fragment(), View.OnClickListener{

    private lateinit var mCallBack : MyFragmentCallBack
    private lateinit var vEmailUser: TextView
    private lateinit var vNameUse : TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.createCallBack()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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




