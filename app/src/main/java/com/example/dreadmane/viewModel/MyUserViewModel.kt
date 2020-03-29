package com.example.dreadmane.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreadmane.data.User
import com.example.dreadmane.fragments.ChapterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyUserViewModel : ViewModel() {

    companion object{
        const val TAG = "ViewModelUser"
    }

    private var authUser = FirebaseAuth.getInstance().currentUser!!
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mAllUsers: MutableLiveData<HashMap<String,User>> = MutableLiveData<HashMap<String,User>>()

    private val users: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUsers()
        }
    }

    init {
        loadAllUsers()
    }



    fun getUsers(): LiveData<User> {
        return users
    }

    fun userList(): MutableLiveData<HashMap<String, User>> {
        return mAllUsers
    }


    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    val post = dataSnapshot.getValue(User::class.java)
                    users.value = post
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        database.child("users").child(authUser.uid).addValueEventListener(postListener)


    }

    private fun loadAllUsers(){

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue(User::class.java)
                if (comment != null) {
                    mAllUsers.value?.put(dataSnapshot.key!!,comment)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue(User::class.java)
                val commentKey = dataSnapshot.key

                if (newComment != null && commentKey != null && mAllUsers.value?.containsKey(commentKey)!!) {
                    mAllUsers.value?.set(commentKey,newComment)
                }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ChapterFragment.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                mAllUsers.value?.remove(commentKey)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue(User::class.java)
                val commentKey = dataSnapshot.key

                if (movedComment != null && commentKey != null && mAllUsers.value?.containsKey(commentKey)!!) {
                    mAllUsers.value?.set(commentKey,movedComment)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //TAG.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        database.child("users").addChildEventListener(childEventListener)
    }
}