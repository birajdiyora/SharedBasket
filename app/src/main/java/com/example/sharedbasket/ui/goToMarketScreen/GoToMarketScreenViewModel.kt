package com.example.sharedbasket.ui.goToMarketScreen

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedbasket.notification.FCMNotificationManager
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.ResultState
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.*

@HiltViewModel
class GoToMarketScreenViewModel @Inject constructor(
    private val FCMNotificationManaer : FCMNotificationManager,
    private val db : FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository
) : ViewModel(){

    lateinit var currentUser : FirebaseUser
    var currentUserLocation : Map<String, Any> = mapOf()
    var currentUserName : String = ""
    val userRef = db.collection("userData")
    init {
        currentUser = FirebaseAuth.getInstance().currentUser!!
        if(currentUser == null){
            Log.d("test","current user is null")
        }else{
            Log.d("test","current user is not null")
        }
        viewModelScope.launch {
            userRef.document(currentUser.uid).get()
                .addOnSuccessListener {document ->
                    if(document.data == null){
                        Log.d("test","${currentUser.uid} User Not Found")
                    }else{
                        Log.d("test","${currentUser.uid} user found")
                    }
                    currentUserLocation = document.data?.get("location") as Map<String, Any>
                    currentUserName = document.data?.get("name") as String
                    Log.d("test", "Location: $currentUserLocation, Name: $currentUserName")
                }
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val doc = it.getResult()
                        Log.d("test", doc.getData().toString())
                    }else{
                        Log.d("test","some error accure")
                    }
                }
        }
    }

   private fun sendNotification(FCMToken : String,marketerName : String,marketName : String,uid : String) {
        FCMNotificationManaer.sendNotification(FCMToken, marketerName, "I am Going To ${marketName}")
       viewModelScope.launch {
           authRepository.insertNotificationData(
               notification = Notification(marketerName = marketerName, marketName = marketName, marketerId = currentUser.uid),
               uid= uid).collect{
               Log.d("test",it.toString())
           }
       }
    }


    suspend fun sendNotificationToAll(marketName : String) : Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        if(currentUser == null){
            Log.d("test","user is null")
        }
        val radius = 1

        userRef.get()
            .addOnSuccessListener {documents ->

                for(document in documents){
                    val userData = document.data
                    val userLocation = userData["location"] as Map<String, Any>
                    val latitude = userLocation["latitude"] as Double
                    val longitude = userLocation["longitude"] as Double
                    val uid = userData["uid"] as String
                    val distance = calculateDistance(latitude, longitude, currentUserLocation["latitude"] as Double, currentUserLocation["longitude"] as Double)

                    if(uid != currentUser.uid) {
                        if (distance <= radius) {
                            Log.d("test", "In If condition")
                            val fcmToken = userData["FCMToken"] as String
                            Log.d("test", fcmToken)
                            sendNotification(
                                FCMToken = fcmToken,
                                marketerName = currentUserName,
                                marketName = "$marketName",
                                uid = uid
                            )
                        }
                    }
                }
                trySend(ResultState.Success("success"))
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }.await()
        awaitClose {
            close()
        }
    }


     private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Radius of the Earth in kilometers
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                        sin(lonDistance / 2) * sin(lonDistance / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
}