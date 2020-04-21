package com.example.dreadmane.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreadmane.data.RdvData
import com.example.dreadmane.fragments.ChapterFragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class RdvListViewModel : ViewModel() {

    companion object{
        const val TAG = "RdvListViewModel"
    }

    private val database = FirebaseDatabase.getInstance().reference

    private var mAllRdv: MutableLiveData<HashMap<String,RdvData>> = MutableLiveData<HashMap<String,RdvData>>()

    private val mRdvList: MutableLiveData<List<RdvData>> by lazy {
        MutableLiveData<List<RdvData>>().also {
            loadRdv()
        }

    }

    init {
        mAllRdv.value = HashMap()
        loadRdvs()
    }

    private val mRdvArrayList = arrayListOf<RdvData>()

    fun getRdv(): LiveData<List<RdvData>> {
        return mRdvList
    }

    fun getAllRdv(): LiveData<HashMap<String, RdvData>> {
        return mAllRdv
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

    private fun loadRdvs() {
        // Do an asynchronous operation to fetch users.

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue(RdvData::class.java)
                if (comment != null) {
                    mAllRdv.value?.put(dataSnapshot.key!!, comment)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

                if (newComment != null && commentKey != null) {
                    mAllRdv.value?.put(commentKey,newComment)
                }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ChapterFragment.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                mAllRdv.value?.remove(commentKey)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ChapterFragment.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue(RdvData::class.java)
                val commentKey = dataSnapshot.key

                if (movedComment != null && commentKey != null){
                    mAllRdv.value?.put(commentKey,movedComment)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        database.child("rdv").addChildEventListener(childEventListener)
    }
}