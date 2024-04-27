package com.example.sharedbasket

import android.content.Intent
import android.os.Bundle
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
import com.example.sharedbasket.ui.receivedRequestDetailScreen.ReceivedRequestDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceivedRequestDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedBasketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReceivedRequestDetailScreen(
                        onGoToHomeActivity = {
                            val intent = Intent(this,HomeActivity::class.java).apply {
                                putExtra("fromRRD",true)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
