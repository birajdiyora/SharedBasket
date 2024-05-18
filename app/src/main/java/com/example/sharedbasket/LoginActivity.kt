package com.example.sharedbasket

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.compose.SharedBasketTheme
import com.example.sharedbasket.navigation.NavGraph
import com.example.sharedbasket.ui.loginScreen.LoginScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    companion object{
        lateinit var userManager: UserManager
        lateinit var firebaseAuth: FirebaseAuth
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        userManager = UserManager(this)
        firebaseAuth = FirebaseAuth.getInstance()
//        firebaseAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this,HomeActivity::class.java))
        }
        setContent {
            SharedBasketTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    LoginScreen(activity = this)
                    val navHostController = rememberNavController()
                    NavGraph(
                        activity = this,
                        navHostController = navHostController,
                        onNavigateToMapActivity = {
                            val intent = Intent(this,MapActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onNavigateToHomeScreen = {
                            val intent = Intent(this,HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        )
                }
            }
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}