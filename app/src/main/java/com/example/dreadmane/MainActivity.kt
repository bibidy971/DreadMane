package com.example.dreadmane

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import sun.jvm.hotspot.utilities.IntArray
import kotlin.math.log


/**
 * Crée par C-H
 */


class MainActivity : Activity(), View.OnClickListener {

    private val TAG = "GoogleActivity"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sign_in_button.setOnClickListener(this)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    public override fun onStart() {
        super.onStart()

        val alreadyLoggedAccount : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (alreadyLoggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show()
            onLoggedIn(alreadyLoggedAccount!!)
        }
        else Log.d(TAG,"Not logged in")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when(requestCode){
                101 -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data);try {
                        val account = task.getResult(ApiException::class.java)
                        onLoggedIn(account!!)
                    }catch (e: ApiException){
                        Log.w(TAG, "Google sign in failed", e)
                    }
                }
            }
        }
    }

    // Start Auth_with_google
    private fun onLoggedIn(googleSignInAccount: GoogleSignInAccount){
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount)
    }

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 101)
    }
    // [END signin]

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sign_in_button -> signIn()
        }
    }


}
