package com.example.sharedbasket.utils

data class Notification(
    val senderName : String,
    val marketName : String,
    val timeStamp : Long = System.currentTimeMillis()
)
