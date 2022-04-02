package com.example.sabbartask.data.remote


import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.sabbartask.data.remote.entities.FirebaseData
import com.example.sabbartask.ui.features.userDriver.databaseReference
import com.example.sabbartask.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteSource @Inject constructor() {

    var state = MutableLiveData<FirebaseData?>()
   suspend fun getData() {
        withContext(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference = firebaseDatabase.getReference(Constants.DATA)
            databaseReference.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(FirebaseData::class.java)
                    state.postValue(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    state.postValue(null)
                }
            })
        }
    }


}