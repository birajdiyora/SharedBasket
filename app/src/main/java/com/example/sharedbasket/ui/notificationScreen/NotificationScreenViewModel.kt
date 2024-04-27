package com.example.sharedbasket.ui.notificationScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.NotificationListState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    val authRepository: AuthRepository,
    auth: FirebaseAuth,
    val db : FirebaseFirestore
) : ViewModel() {
    val currentUser = auth.currentUser
    private val _notificationListState = MutableStateFlow(NotificationListState())
    val notificationListState = _notificationListState.asStateFlow()

    private var _requestState = MutableStateFlow("")
    val requestState = _requestState.asStateFlow()

    private var _notificationId = MutableStateFlow("")
    val notificationId = _notificationId.asStateFlow()

    init {
        updateNotificationList()
        db.collection("userData").document(currentUser!!.uid).addSnapshotListener { snapshot, error ->
//            snapshot!!.reference.get().addOnSuccessListener {
//              Log.d("test",it.toString())
//            }}
            updateNotificationList()
        }
        viewModelScope.launch {
            val FCMToken = FirebaseMessaging.getInstance().token.await()
            Log.d("test",FCMToken)
           updateFCMTokem(FCMTOken = FCMToken).collect{
                Log.d("test",it.toString())
            }
        }
    }

    private fun updateRequestStatus() {

    }
    private fun updateFCMTokem(FCMTOken : String) =
        authRepository.updateFCMToken(FCMTOken)
    private fun updateNotificationList() {
        db.collection("userData").document(currentUser!!.uid).get()
            .addOnSuccessListener {document->
                Log.d("test3","in Database")
                Log.d("test3",document["notificationList"].toString())
                if(document["notificationList"]!=null) {
                    _notificationListState.update {
                        it.copy(
                            notificationList = convertToNotificationList(document["notificationList"] as List<HashMap<String, Any>>)
                        )
                    }
                }
            }
    }
    fun convertToNotificationList(hashMapList: List<HashMap<String, Any>>): List<Notification> {
        val notificationList = mutableListOf<Notification>()
        for (hashMap in hashMapList) {
            val notificationId = hashMap["notificationId"] as String
            val marketerName = hashMap["marketerName"] as String
            val marketName = hashMap["marketName"] as String
            val timeStamp = hashMap["timeStamp"] as Long
            val marketerId = hashMap["marketerId"] as String
            val status = hashMap["status"] as String
            val notification = Notification(notificationId = notificationId, marketerId = marketerId, marketerName = marketerName, marketName =  marketName, timeStamp =  timeStamp, status = status)
            notificationList.add(notification)
        }
        return notificationList
    }
}