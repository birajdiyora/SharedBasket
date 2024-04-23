package com.example.sharedbasket.repository

import android.app.Activity
import android.util.Log
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.ResultState
import com.example.sharedbasket.utils.ResultExist
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authDb : FirebaseAuth,
    private val db : FirebaseFirestore
) : AuthRepository {
    val currentUser = authDb.currentUser
    private lateinit var verificationCode : String
    override fun createUserWithPhone(phone: String,activity: Activity): Flow<ResultState<String>> = callbackFlow {

        val callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                trySend(ResultState.Failure(p0))
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                trySend(ResultState.Success("Otp send successfuly"))
                verificationCode = p0
                Log.d("test",p0)
            }
        }
        trySend(ResultState.Loading)
        val options = PhoneAuthOptions.newBuilder(authDb)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose {
            close()
        }
    }

    override fun signWithCredential(otp: String): Flow<ResultState<String>>  = callbackFlow{
        trySend(ResultState.Loading)
        val credential = PhoneAuthProvider.getCredential(verificationCode,otp)
        authDb.firebaseAuthSettings.forceRecaptchaFlowForTesting(false)
        authDb.signInWithCredential(credential)
            .addOnCompleteListener {
//                if(it.result.additionalUserInfo?.isNewUser == false){
//                    "User Exist".toast()
//                }
//                it.result.additionalUserInfo?.isNewUser.toast()
                if(it.isSuccessful){
                    trySend(ResultState.Success("OTP Verified"))
                }else{
                    Log.d("test","not auth")
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun isUserAlreadyRegister(mobileNo : String) : Flow<ResultExist>   = callbackFlow {
        db.collection("user").document(mobileNo).get()
            .addOnSuccessListener {
                if(it.exists()){
                    trySend(ResultExist.Exist)
                }else{
                    trySend(ResultExist.notExist)
                }
            }
            .addOnFailureListener {
                trySend(ResultExist.Error)
            }
        awaitClose {
            close()
        }
    }

    override fun insertUserData(user: HashMap<String, Any>): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
         db.collection("userData").document(currentUser!!.uid).set(user)
             .addOnCompleteListener {
                 trySend(ResultState.Success(it.toString()))
             }
             .addOnFailureListener {
                 trySend(ResultState.Failure(it))
             }
        db.collection("user").document(user.get("mobileNo").toString()).set(user)
        awaitClose {
            close()
        }
    }

    override fun updateFCMToken(FCMToken : String): Flow<ResultState<String>> = callbackFlow{
        if(currentUser!=null){
            db.collection("userData").document(currentUser!!.uid).update(
                mapOf(
                    "FCMToken" to FCMToken
                )
            )
                .addOnCompleteListener {
                    Log.d("test","Updated..")
                    trySend(ResultState.Success(it.toString()))
                }
                .addOnFailureListener {
                    Log.d("test",it.toString())
                    trySend(ResultState.Failure(it))
                }
        }else{
            "some error".toast()
        }

        awaitClose {
            close()
        }
    }

    override fun insertNotificationData(uid : String,notification: Notification): Flow<ResultState<String>> = callbackFlow{
        Log.d("test","insertNotification")
        trySend(ResultState.Loading)
        val userRef = db.collection("userData").document(uid)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val notificationList = mutableListOf<Notification>()

                if (documentSnapshot.exists()) {
                    val existingNotifications = documentSnapshot.data?.get("notificationList") as? List<HashMap<String, Any>>
                    existingNotifications?.forEach { notificationData ->
                        val notificationId = notificationData["notificationId"] as String
                        val senderName = notificationData["senderName"] as String
                        val marketName = notificationData["marketName"] as String ?: ""
                        val timestamp = notificationData["timeStamp"] as Long
                        val senderUID = notificationData["senderUID"] as String
                        val status = notificationData["status"] as String
                        val existingNotification =
                            Notification(notificationId = notificationId,senderUID = senderUID, senderName = senderName, marketName = marketName, timeStamp =  timestamp, status = status)
                        notificationList.add(existingNotification)
                    }
                }

                notificationList.add(notification)

                val updatedData = hashMapOf(
                    "notificationList" to notificationList
                )

                userRef.update(updatedData as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d("test", "Notification stored successfully for user")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("test", "Error storing notification for user", exception)
                    }
                trySend(ResultState.Success("success"))
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Failure(exception))
                Log.e("test", "Error retrieving user document for user", exception)
            }
        awaitClose {
            close()
        }
    }

    override fun insertRequestData(notificationId:String,data: HashMap<String, Any>): Flow<ResultState<String>> =
        callbackFlow{
            trySend(ResultState.Loading)
            db.collection("requestItem").document(notificationId).set(data)
                .addOnCompleteListener {
                    trySend(ResultState.Success(it.toString()))
                }
                .addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
            val userDatRef = db.collection("userData").document(data["senderId"].toString())

            userDatRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val notificationList =
                        documentSnapshot.get("notificationList") as MutableList<Map<String, Any>>?
                    notificationList?.let { list ->
                        val updatedList = mutableListOf<Map<String, Any>>()
                        for (notification in list) {
                            if (notification["notificationId"] == notificationId) {
                                // Update the status of the notification
                                val updatedNotification = notification.toMutableMap()
                                updatedNotification["status"] = "pending"
                                updatedList.add(updatedNotification)
                            } else {
                                updatedList.add(notification)
                            }
                        }
                        userDatRef.update("notificationList", updatedList)
                            .addOnSuccessListener {
                                // Notification status updated successfully
                                Log.d("test", "Notification status updated successfully!")
                            }
                            .addOnFailureListener { e ->
                                // Error updating notification status
                                Log.w("test", "Error updating notification status", e)
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d("test", "Error getting document", it)
                }
            awaitClose {
                close()
            }
    }
}