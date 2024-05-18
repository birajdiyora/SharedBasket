package com.example.sharedbasket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    auth: FirebaseAuth,
    db : FirebaseFirestore
) : ViewModel(){
//    var _userName = MutableStateFlow("")
//    val userName = _userName.asStateFlow()
//
    var username by mutableStateOf("")
    val currentUser = auth.currentUser
    init {
        db.collection("userData").document(currentUser!!.uid).get().addOnSuccessListener {document ->
            username = document["name"].toString()
        }
            .addOnFailureListener {

            }
    }
}