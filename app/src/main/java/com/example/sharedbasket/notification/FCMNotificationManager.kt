package com.example.sharedbasket.notification

import android.util.Log
import com.example.sharedbasket.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class FCMNotificationManager {

    fun sendNotification(FCMToken : String,senderName : String,message : String) {
        val JsonObject = JSONObject()
        val notificationObj = JSONObject()
        val dataObj = JSONObject()
        notificationObj.put("title",senderName)
        notificationObj.put("body",message)
        notificationObj.put("android_channel_id", NotificationChannel.CHANNEL_ID)
        notificationObj.put("sound", "android.resource://com.example.sharedbasket/${R.raw.notification_tone}")
//        notificationObj.put("priority", "high")
        dataObj.put("userId","12365")


        JsonObject.put("notification",notificationObj)
        JsonObject.put("data",dataObj)
        JsonObject.put("to",FCMToken)
        callApi(JsonObject)
    }

    fun callApi(JsonObject : JSONObject) {
        val JSON  = "application/json; charset=utf-8".toMediaType()
        val client : OkHttpClient = OkHttpClient()
        val url = "https://fcm.googleapis.com/fcm/send"
        val body = JsonObject.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .header("Authorization","Bearer AAAAcLorBLU:APA91bEcRiTN6pnEJqE0XiGoIhIcFnZ-mPjXRAtsBziELNUdcA2oTkz--x2-xIeNkA-6ZZWD_UgtJxiQw8OlKh0XNKHXizdfig22TfU1FYaxovHOWZXAYnD09bjkHpFh4pOJb5c3TZ3U")
            .header("Content-Type", "application/json")
            .header("Priority", "high")
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("test","message send is failure")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("test","message send completed...")
            }

        })
    }
}