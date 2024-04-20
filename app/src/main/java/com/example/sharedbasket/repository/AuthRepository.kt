package com.example.sharedbasket.repository

import android.app.Activity
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.ResultState
import com.example.sharedbasket.utils.ResultExist
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun createUserWithPhone(phone:String,activity: Activity) : Flow<ResultState<String>>

    fun signWithCredential(otp:String) : Flow<ResultState<String>>

    fun isUserAlreadyRegister(otp: String) : Flow<ResultExist>

    fun insertUserData(user : HashMap<String,Any>) : Flow<ResultState<String>>

    fun updateFCMToken(FCMToken : String) : Flow<ResultState<String>>

    fun insertNotificationData(uid : String,notification: Notification) : Flow<ResultState<String>>
}