package com.example.sharedbasket.ui.receivedRequestDetailScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.Item
import com.example.sharedbasket.utils.ReceivedRequestState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceivedRequestDetailScreenViewModel @Inject constructor (
    val authRepository: AuthRepository,
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
        viewModelScope.launch {
            authRepository.updateItemStatusInUserData(
                itemsRequesterId = receivedRequestState.itemsRequesterId,
                notificationId = receivedRequestState.notificationId,"confirm").collect{

            }
        }

    }
}