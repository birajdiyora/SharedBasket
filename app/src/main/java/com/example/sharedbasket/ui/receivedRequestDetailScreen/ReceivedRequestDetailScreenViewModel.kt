package com.example.sharedbasket.ui.receivedRequestDetailScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.Item
import com.example.sharedbasket.utils.ReceivedRequestState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReceivedRequestDetailScreenViewModel @Inject constructor (
    authRepository: AuthRepository,
    val savedStateHandle: SavedStateHandle,
    val db : FirebaseFirestore
) : ViewModel(){
    val receivedRequestState = savedStateHandle.get<ReceivedRequestState>("receivedRequest")
    init {
        Log.d("test",receivedRequestState.toString())
    }

    fun updateItemPrice(items : List<Item>) {
        db.collection("requestItem").document(receivedRequestState!!.notificationId).update(
            hashMapOf(
                "items" to items,
                "status" to "confirm"
            )
        )
            .addOnCompleteListener {

            }
            .addOnFailureListener {

            }
        //update status in userData

        val userDatRef = db.collection("userData").document(receivedRequestState.itemsRequesterId)
        userDatRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val notificationList =
                    documentSnapshot.get("notificationList") as MutableList<Map<String, Any>>?
                Log.d("test",notificationList.toString())
                notificationList?.let { list ->
                    val updatedList = mutableListOf<Map<String, Any>>()
                    for (notification in list) {
                        if (notification["notificationId"] == receivedRequestState.notificationId) {
                            // Update the status of the notification
                            val updatedNotification = notification.toMutableMap()
                            updatedNotification["status"] = "confirm"
                            updatedList.add(updatedNotification)
                        } else {
                            Log.d("test","in notification status update ${notification}")
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
                            Log.d("test", "Error updating notification status", e)
                        }
                }
            }
            .addOnFailureListener {
                Log.d("test", "Error getting document", it)
            }
    }
}