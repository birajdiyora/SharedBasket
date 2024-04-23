package com.example.sharedbasket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.SharedBasketTheme
import com.example.sharedbasket.ui.sendRequestScreeen.SendRequestScreen
import com.example.sharedbasket.utils.Notification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendRequestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notification = intent.getParcelableExtra<Notification>("notification")
        Log.d("test",notification.toString())
        setContent {
            SharedBasketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (notification != null) {
                        SendRequestScreen(
                            notification = notification,
                            onGoToHomeActivity = {
                                val intent = Intent(this@SendRequestActivity,HomeActivity::class.java)
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}