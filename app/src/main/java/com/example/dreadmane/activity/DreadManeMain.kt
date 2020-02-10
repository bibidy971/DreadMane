package com.example.dreadmane.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dreadmane.R
import com.example.dreadmane.fragments.BlogFragment
import com.example.dreadmane.fragments.ChapterFragment
import com.example.dreadmane.fragments.StoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dread_mane_main.*


class DreadManeMain : AppCompatActivity(),StoreFragment.MyFragmentCallBack {

    private lateinit var authUser : FirebaseUser

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



