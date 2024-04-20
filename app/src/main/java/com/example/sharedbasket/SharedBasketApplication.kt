package com.example.sharedbasket

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.sharedbasket.common.app
import com.example.sharedbasket.common.appContext
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltAndroidApp
class SharedBasketApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        app = this
        appContext = base
    }
}

class UserManager(val context: Context) {

    companion object{
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("usertoken")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_MOBILE_NUMBER_KEY = stringPreferencesKey("user_number")
        private val USER_LOCATION_KEY = stringPreferencesKey("lat_lng_key")
        private val USER_FCM_TOKEN = stringPreferencesKey("fcm_token")
    }

    suspend fun storeUserName(userName : String) {
        context.dataStore.edit {
            it[USER_NAME_KEY] = userName
        }
    }
    suspend fun storeFCmKey(FCMToken : String) {
        context.dataStore.edit {
            it[USER_FCM_TOKEN] = FCMToken
        }
    }
    suspend fun storeUserMobileNumber(userMobileNo : String) {
        context.dataStore.edit {
            it[USER_MOBILE_NUMBER_KEY] = userMobileNo
        }
    }
    private val gson = Gson()
    suspend fun storeUserLocation(latLng: LatLng) {
        val jsonString = gson.toJson(latLng)
        context.dataStore.edit { preferences ->
            preferences[USER_LOCATION_KEY] = jsonString
        }
    }

    fun getUserLocation(): Flow<LatLng?> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[USER_LOCATION_KEY]
            if (jsonString != null) {
                gson.fromJson(jsonString, LatLng::class.java)
            } else {
                null
            }
        }
    }

    val userName : Flow<String> = context.dataStore.data.map {
        it[USER_NAME_KEY] ?: ""
    }
    val userMobileNumber : Flow<String> = context.dataStore.data.map {
        it[USER_MOBILE_NUMBER_KEY] ?: ""
    }
    val userFCMToken : Flow<String> = context.dataStore.data.map {
        it[USER_FCM_TOKEN] ?: ""
    }
//    val userLocation : Flow<String> = context.dataStore.data.map {
//        it[USER_LOCATION_KEY] ?: ""
//    }
}

fun timestampToDateTimeString(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault())
    return formatter.format(date)
}