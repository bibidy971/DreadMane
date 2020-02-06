package com.example.dreadmane.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dreadmane.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_utilisateur.*


class DreadManeMain : AppCompatActivity(), View.OnClickListener {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_utilisateur)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        sign_out.setOnClickListener(this)
        setDataOnView()

    }

    private fun setDataOnView(){

        val googleSignInAccout = intent.getParcelableExtra<GoogleSignInAccount>(GOOGLE_ACCOUNT)

        Picasso.get().load(googleSignInAccout.photoUrl).centerInside().fit().into(profile_image)
        profile_text.text = googleSignInAccout.displayName
        profile_email.text = googleSignInAccout.email
    }

    companion object {
        const val GOOGLE_ACCOUNT = "google_account"
        private const val TAG = "DreadManeActivity"
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sign_out -> signOut()

        }
    }

    private fun signOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void?> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            })
    }
}


