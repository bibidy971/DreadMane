package com.example.dreadmane.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dreadmane.R
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.fragments.BlogFragment
import com.example.dreadmane.fragments.ChapterFragment
import com.example.dreadmane.fragments.StoreFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dread_mane_main.*
import java.io.Serializable


class DreadManeMain : AppCompatActivity(),StoreFragment.MyFragmentCallBack, ChapterFragment.MyFragmentCallBack {

    private lateinit var authUser : FirebaseUser
    private lateinit var database: DatabaseReference


    companion object {
        const val GOOGLE_ACCOUNT = "google_account"
        private const val TAG = "DreadManeActivity"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dread_mane_main)

        database = FirebaseDatabase.getInstance().reference
        authUser = FirebaseAuth.getInstance().currentUser!!

        val name = authUser.displayName?.split(" ")

        initFragments(savedInstanceState)
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

    override fun disconnection() {
        signOut()
    }

    override fun addRdvButton() {
        val signInIntent = Intent(this, AddRdvActivity::class.java)
        startActivity(signInIntent)
    }

    override fun infoRdv(rdvData: RdvData) {
        val signInIntent = Intent(this, InfoRdvActivity::class.java)
        signInIntent.putExtra("rdv",rdvData as Serializable)
        startActivity(signInIntent)
    }

}



