package com.example.dreadmane.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dreadmane.R
import com.example.dreadmane.data.User
import com.example.dreadmane.fragments.ChapterFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_google.*
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Crée par C-H
 */


class MainActivity : Activity(), View.OnClickListener {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

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

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        database = FirebaseDatabase.getInstance().reference

    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) onLoggedIn() else Log.d(TAG,"Not logged in")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        when(requestCode){
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                }catch (e: ApiException){
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    if (user != null) {



                        val postListener = object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    val post = p0.getValue(User::class.java)
                                    if (post?.username == null) {
                                        post?.username = acct.displayName
                                    }
                                    if (post?.email == null) {
                                        post?.email = acct.email
                                    }
                                    if (post?.admin == null) {
                                        post?.admin = false
                                    }
                                    if (post?.uriPhoto == null) {
                                        post?.uriPhoto = acct.photoUrl.toString()
                                        database.child("users").child(user.uid).setValue(post)
                                    }
                                }else{
                                    database.child("users").child(user.uid).setValue(User(acct.displayName,user.uid,acct.email,acct.photoUrl.toString()))
                                }
                            }
                        }
                        database.child("users").child(user.uid).addListenerForSingleValueEvent(postListener)
                    }


                    val intent = Intent(this, DreadManeMain::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
    // [END auth_with_google]

    // Start Auth_with_google
    private fun onLoggedIn(){

        val intent = Intent(this, DreadManeMain::class.java)
        startActivity(intent)
        finish()
    }

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sign_in_button -> signIn()
        }
    }

    companion object{
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleActivity"
    }


}
