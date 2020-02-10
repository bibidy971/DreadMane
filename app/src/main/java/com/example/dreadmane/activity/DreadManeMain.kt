package com.example.dreadmane.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dreadmane.R
import com.example.dreadmane.data.User
import com.example.dreadmane.fragments.BlogFragment
import com.example.dreadmane.fragments.ChapterFragment
import com.example.dreadmane.fragments.StoreFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.dread_mane_main.*


class DreadManeMain : AppCompatActivity(),StoreFragment.MyFragmentCallBack {

    private lateinit var authUser : FirebaseUser
    private lateinit var database: DatabaseReference

    companion object {
        const val GOOGLE_ACCOUNT = "google_account"
        private const val TAG = "DreadManeActivity"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dread_mane_main)

        authUser = intent.getParcelableExtra(GOOGLE_ACCOUNT)

        eMailUser = authUser.email.toString()
        photoUser = authUser.photoUrl

        val name = authUser.displayName?.split(" ")
        nameUser = name?.get(0) ?: " "

        initFragments(savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        /*val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })*/

        writeNewUser(authUser.uid, authUser.displayName.toString(), authUser.email.toString())



    }

    override fun onStart() {
        super.onStart()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(User::class.java)

                Toast.makeText(applicationContext, "database : ${post?.email}", Toast.LENGTH_LONG).show()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        database.child("users").child(authUser.uid).addValueEventListener(postListener)

        writeNewUser(authUser.uid, authUser.displayName.toString(), "charles")

    }


    private fun writeNewUser(userId: String, name: String, email: String?) {
        val user = User(name, email)
        database.child("users").child(userId).setValue(user)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_blog -> {
                val fragment = BlogFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chapter -> {
                val fragment = ChapterFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_store -> {
                val fragment = StoreFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun initFragments(savedInstanceState: Bundle?){
        setSupportActionBar(toolbar)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            val fragment = BlogFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
    }


    private fun revokeAccess() {
        val curentUser = FirebaseAuth.getInstance()
        curentUser.currentUser?.delete()
        curentUser.signOut()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()


        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInClient.signOut()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override var eMailUser: String = ""
    override var nameUser: String = ""
    override var photoUser: Uri? = null

    override fun disconnection() {
        signOut()
    }



}



