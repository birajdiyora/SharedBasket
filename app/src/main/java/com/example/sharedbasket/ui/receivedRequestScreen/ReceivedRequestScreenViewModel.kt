package com.example.sharedbasket.ui.receivedRequestScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.ui.sendRequestScreeen.convertHashMapListToItemList
import com.example.sharedbasket.utils.ReceivedRequestState
import com.example.sharedbasket.utils.ReceivedRequestStateList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ReceivedRequestScreenViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val db : FirebaseFirestore,
    val auth : FirebaseAuth
) : ViewModel() {
    val currentUser = auth.currentUser

    private val _receivedRequestStateList = MutableStateFlow(ReceivedRequestStateList())
    val receivedRequestStateList = _receivedRequestStateList.asStateFlow()

    init {

        viewModelScope.launch {
            updateReceivedrequestData()
            db.collection("userData").addSnapshotListener { value, error ->
                viewModelScope.launch {
                    updateReceivedrequestData()
                }
            }
        }
    }


    private suspend fun updateReceivedrequestData() {
        db.collection("requestItem")
            .whereEqualTo("marketerId", currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
//                Log.d("test5",documents.documents[0].toString())
                viewModelScope.launch {
                    val receivedRequests = mutableListOf<ReceivedRequestState>()
                    for (document in documents) {
                        val data = document.data
                        var itemsRequestername = mutableStateOf("")
                        db.collection("userData").document(data["itemsRequesterId"] as String)
                            .get()
                            .addOnSuccessListener { document ->
                                itemsRequestername.value = document["name"] as String
                            }
                            .addOnFailureListener {

                            }.await()
                        Log.d("test5", data.toString())
                        val receivedRequestState = ReceivedRequestState(
                            notificationId = document.id,
                            marketerId = data["marketerId"] as String,
                            itemsRequesterId = data["itemsRequesterId"] as String,
                            itemsRequesterName = itemsRequestername.value,
                            status = data["status"] as String,
                            marketName = data["marketName"] as String,
                            items = convertHashMapListToItemList(data["items"] as List<HashMap<String, Any>>),
                            timeStamp = data["timeStamp"] as Long
                        )
                        receivedRequests.add(receivedRequestState)
                        Log.d("test5",receivedRequestState.itemsRequesterId)
                    }
                    _receivedRequestStateList.update {
                        it.copy(
                            list = receivedRequests
                        )
                    }
                }
            }
            .addOnFailureListener {

            }
    }
}