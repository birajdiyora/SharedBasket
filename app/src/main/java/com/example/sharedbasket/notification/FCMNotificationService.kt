package com.example.sharedbasket.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FCMNotificationService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("test","new token received")
    }

}