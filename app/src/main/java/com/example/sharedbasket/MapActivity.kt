package com.example.sharedbasket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.compose.SharedBasketTheme
import com.example.sharedbasket.common.CommonDialog
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.utils.ResultState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity () : ComponentActivity() {
    @Inject
    lateinit var  repository: AuthRepository
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationCallback : LocationCallback
    private var locationRequired : Boolean = false
    private val permissions = listOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    
    override fun onResume() {
        super.onResume()
        if(locationRequired)
            startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateAgeMillis(100)
                .build()
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(this,MapsInitializer.Renderer.LATEST){

        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            SharedBasketTheme {
                var currentLocation by remember {
                    mutableStateOf(LatLng(0.toDouble(),0.toDouble()))
                }
                var cameraPositionState = rememberCameraPositionState{
                    position = CameraPosition.fromLatLngZoom(
                        currentLocation,17f
                    )
                }
                var cameraPosition by remember {
                    mutableStateOf(cameraPositionState)
                }

                locationCallback = object : LocationCallback(){
                    override fun onLocationResult(p0: LocationResult) {
                        super.onLocationResult(p0)
                        for(location in p0.locations){
//                            Log.d("test","Hello")
                            currentLocation = LatLng(location.latitude,location.longitude)
                            cameraPositionState = CameraPositionState(
                                position = CameraPosition.fromLatLngZoom(
                                    currentLocation,17f
                                )
                            )
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isDialog by remember {
                        mutableStateOf(false)
                    }
                    if(isDialog)
                        CommonDialog()

                LocationScreen(context = this@MapActivity,
                    currentLocation = currentLocation,
                    cameraPositionState = cameraPositionState,
                    isDialogStateChange = {
                        isDialog = it
                    })
                }
            }
        }
    }
    @Composable
    private fun LocationScreen(context: Context,currentLocation:LatLng,
                               cameraPositionState: CameraPositionState,
                               isDialogStateChange : (Boolean) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val areGranted = permissions.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                locationRequired = true
                startLocationUpdate()
                "permission granted".toast()
            } else {
                "permission denied".toast()
            }
        }
        Scaffold(
            modifier = Modifier,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    var name : String = ""
                    var mobileNo : String = ""
                    var location : LatLng = LatLng(0.0,0.0)
                    var FCMToken : String = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        LoginActivity.userManager.storeUserLocation(currentLocation)
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        LoginActivity.userManager.userName.collect {
                            name = it
                        }
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        LoginActivity.userManager.userMobileNumber.collect {
                            mobileNo = it
                        }
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        LoginActivity.userManager.userFCMToken.collect {
                            FCMToken = it
                        }
                    }
                    location = currentLocation
//                    CoroutineScope(Dispatchers.Main).launch{
//                    LoginActivity.userManager.getUserLocation().collect {
//                            if (it != null) {
//                                location = it
//                            }
//                        }
//                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        val user = hashMapOf(
                            "uid" to currentUser!!.uid,
                            "name" to name,
                            "mobileNo" to mobileNo,
                            "location" to location,
                            "FCMToken" to FCMToken
                        )
                        repository.insertUserData(user).collect{
                            when(it){
                                is ResultState.Loading ->{
                                    Log.d("test","Loading")
                                    isDialogStateChange(true)
                                }
                                is ResultState.Success -> {
                                    Log.d("test","Success")
                                    "Register Successfuly".toast()
                                    isDialogStateChange(false)
                                    val intent = Intent(context,HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                is ResultState.Failure ->{
                                    Log.d("test","Failed")
                                    "Register Failed".toast()
                                    isDialogStateChange(false)
                                }
                            }
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = false)
                ) {
                    Marker(
                        state = MarkerState(
                            position = currentLocation
                        ),
                        title = "You",
                        snippet = "You are here!!"
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "${currentLocation.latitude}/${currentLocation.longitude}")
                    Button(onClick = {
                        if (permissions.all {
                                ContextCompat.checkSelfPermission(
                                    context, it
                                ) == PackageManager.PERMISSION_GRANTED
                            }) {
                            startLocationUpdate()
                        } else {
                            launcherMultiplePermissions.launch(permissions.toTypedArray())
                        }
                    }) {
                        Text(text = "Get Current Location")
                    }
                }
            }
        }
    }
}
