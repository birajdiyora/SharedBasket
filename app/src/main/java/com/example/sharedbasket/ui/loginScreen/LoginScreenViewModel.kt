package com.example.sharedbasket.ui.loginScreen

import android.app.Activity
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.ResultExist
import com.example.sharedbasket.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val repository: AuthRepository,
    savedStateHandle: SavedStateHandle,
    private val db : FirebaseFirestore,
    val authDb: FirebaseAuth
) : ViewModel() {
//    val userId = auth.currentUser!!.uid
    fun createUserWithPhone(mobile : String,activity: Activity) =
        repository.createUserWithPhone(phone = mobile, activity = activity)

    fun signInWithCredential(code:String) =
        repository.signWithCredential(otp = code)

    fun isUserAlreadyRegister(mobileNo : String) : Flow<ResultExist>{
        return repository.isUserAlreadyRegister(mobileNo)
    }

    fun updateFCMTokem(FCMTOken : String) =
        repository.updateFCMToken(FCMTOken)

}