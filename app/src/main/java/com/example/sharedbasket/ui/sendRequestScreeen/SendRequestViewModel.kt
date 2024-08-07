package com.example.sharedbasket.ui.sendRequestScreeen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.Item
import com.example.sharedbasket.utils.ItemListState
import com.example.sharedbasket.utils.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendRequestViewModel @Inject constructor(
    val authRepository: AuthRepository,
    private val db : FirebaseFirestore,
    private val auth: FirebaseAuth,
    val savedStateHandle: SavedStateHandle
) : ViewModel(){
    val currentUser = auth.currentUser
    val notification = savedStateHandle.get<Notification>("notification")

    private val _itemListState = MutableStateFlow(ItemListState())
    val itemListState = _itemListState.asStateFlow()

    init {
        db.collection("requestItem").document(notification!!.notificationId).get()
            .addOnSuccessListener {document ->
                Log.d("test","${document.data}")
                if(document["items"] != null) {
                    _itemListState.update {
                        it.copy(
                            itemList = convertHashMapListToItemList(document["items"] as List<HashMap<String, Any>>)
                        )
                    }
                }
            }
    }
    fun insertItemRequestData(notification: Notification,items : List<Item>) = authRepository.insertRequestData(
        notificationId = notification.notificationId,
        hashMapOf(
            "notificationId" to notification.notificationId,
            "itemsRequesterId" to currentUser!!.uid,
            "marketerId" to notification.marketerId,
            "status" to "pending",
            "marketName" to notification.marketName,
            "items" to items,
            "timeStamp" to System.currentTimeMillis()
        )
    )

    fun updateItemStatus(status : String) {
        viewModelScope.launch {
            authRepository.updateItemStatusInUserData(
                itemsRequesterId = currentUser!!.uid,
                notificationId = notification!!.notificationId,
                status = status
            ).collect {

            }
        }
        viewModelScope.launch {
            authRepository.updateItemStatusInRequestItem(
                notificationId = notification!!.notificationId,
                status = status
            ).collect{

            }
        }
    }
}
fun convertHashMapListToItemList(hashMapList: List<HashMap<String, Any>>): List<Item> {
    val itemList = mutableListOf<Item>()

    for (hashMap in hashMapList) {
        val itemDescription = hashMap["itemDescription"] as? String ?: ""
        val itemName = hashMap["itemName"] as? String ?: ""
        val itemPrice = (hashMap["itemPrice"] as? String) ?: ""

        val item = Item(itemName, itemDescription,itemPrice)
        itemList.add(item)
    }

    return itemList
}