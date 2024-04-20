package com.example.sharedbasket.ui.notificationScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.NotificationListState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    authRepository: AuthRepository,
    auth: FirebaseAuth,
    db : FirebaseFirestore
) : ViewModel() {
    val currentUser = auth.currentUser
    private val _notificationListState = MutableStateFlow(NotificationListState())
    val notificationListState = _notificationListState.asStateFlow()

    init {
        db.collection("userData").document(currentUser!!.uid).get()
            .addOnSuccessListener {document->
                Log.d("test","in Database")
                Log.d("test",document["notificationList"].toString())
                _notificationListState.update {
                    it.copy(
                        notificationList = convertToNotificationList(document["notificationList"] as List<HashMap<String, Any>>)
                    )
                }
            }
    }

    fun convertToNotificationList(hashMapList: List<HashMap<String, Any>>): List<Notification> {
        val notificationList = mutableListOf<Notification>()
        for (hashMap in hashMapList) {
            val senderName = hashMap["senderName"] as String
            val marketName = hashMap["marketName"] as String
            val timeStamp = hashMap["timeStamp"] as Long
            val notification = Notification(senderName, marketName, timeStamp)
            notificationList.add(notification)
        }
        return notificationList
    }
}