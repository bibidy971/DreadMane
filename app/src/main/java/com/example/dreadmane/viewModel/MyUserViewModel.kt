package com.example.dreadmane.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreadmane.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyUserViewModel : ViewModel() {

    companion object{
        const val TAG = "ViewModelUser"
    }

    private val users: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUsers()
        }
    }

    private val mUserList: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            userList()
        }

    }

    private var authUser = FirebaseAuth.getInstance().currentUser!!
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getUsers(): LiveData<User> {
        return users
    }

    private fun userList(){

    }


    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(User::class.java)
                users.value = post
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        database.child("users").child(authUser.uid).addValueEventListener(postListener)


    }
}