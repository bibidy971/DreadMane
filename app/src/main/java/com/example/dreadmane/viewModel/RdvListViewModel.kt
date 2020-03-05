package com.example.dreadmane.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.data.User
import com.example.dreadmane.fragments.ChapterFragment
import com.google.firebase.database.*

class RdvListViewModel : ViewModel() {

    companion object{
        const val TAG = "RdvListViewModel"
    }

    private val database = FirebaseDatabase.getInstance().reference

    private val mRdvList: MutableLiveData<List<RdvData>> by lazy {
        MutableLiveData<List<RdvData>>().also {
            loadRdv()
        }

    }

    private val mRdvArrayList = arrayListOf<RdvData>()

    fun getRdv(): LiveData<List<RdvData>> {
        return mRdvList
    }

    override fun onCleared() {
        super.onCleared()
        database.child("rdv").setValue(mRdvArrayList)
    }

    fun deleteRDV(rdvData: RdvData){
        mRdvArrayList.remove(rdvData)
        mRdvList.value = mRdvArrayList
    }

    fun takeRdv(oldRdvData: RdvData, uid : String){
        if (mRdvArrayList.contains(oldRdvData)){
            mRdvArrayList[mRdvArrayList.indexOf(oldRdvData)].client = uid
            mRdvList.value = mRdvArrayList
        }
    }

    fun annuleRdv(rdvData: RdvData){
        if (mRdvArrayList.contains(rdvData)){
            mRdvArrayList[mRdvArrayList.indexOf(rdvData)].client = null
            mRdvList.value = mRdvArrayList
        }
    }

    private fun loadRdv() {
        // Do an asynchronous operation to fetch users.

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue(RdvData::class.java)
                if (comment != null && !mRdvArrayList.contains(comment)) {
                    mRdvArrayList.add(comment)
                    mRdvList.value = mRdvArrayList
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

               if (newComment != null && commentKey != null) {
                   if (commentKey.toInt() < mRdvArrayList.size && mRdvArrayList[commentKey.toInt()] != newComment) {
                       mRdvArrayList[commentKey.toInt()] = newComment
                   }
                   mRdvList.value = mRdvArrayList
               }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ChapterFragment.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                mRdvArrayList.remove(dataSnapshot.getValue(RdvData::class.java))
                mRdvList.value = mRdvArrayList
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        database.child("rdv").addChildEventListener(childEventListener)
    }
}