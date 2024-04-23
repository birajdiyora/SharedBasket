package com.example.sharedbasket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.compose.SharedBasketTheme
import com.example.sharedbasket.navigation.BottomNavGraph
import com.example.sharedbasket.navigation.BottomNavScreen
import com.example.sharedbasket.ui.homeScreen.HomeScreen
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedBasketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()
                    var currentScreen by remember {
                        mutableStateOf(BottomNavScreen.NotificationScreen.route)
                    }
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(title = {
                                Text(
                                    text = "SharedBasket",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .shadow(4.dp))
                        },
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier,
                                containerColor = Color.Transparent
                            ) {
                                NavigationBarItem(
                                    modifier = Modifier,
                                    selected = currentScreen == BottomNavScreen.NotificationScreen.route,
                                    onClick = {
                                        currentScreen = BottomNavScreen.NotificationScreen.route
                                        navHostController.navigate(BottomNavScreen.NotificationScreen.route)
                                    },
                                    icon = {
                                           Icon(imageVector = Icons.Default.Notifications,
                                               contentDescription = null)
                                    },
                                    label = {
                                        Text(text = "Notifications")
                                    },)
                                NavigationBarItem(
                                    selected = currentScreen == BottomNavScreen.GoToMarketScreen.route,
                                    onClick = {
                                        currentScreen = BottomNavScreen.GoToMarketScreen.route
                                        navHostController.navigate(BottomNavScreen.GoToMarketScreen.route)
                                    },
                                    icon = { 
                                           Icon(painter = painterResource(id = R.drawable.store),
                                               contentDescription = null,
                                               modifier = Modifier
                                                   .size(22.dp))
                                           },
                                    label = {
                                        Text(text = "Go to market")
                                    })
                                NavigationBarItem(
                                    selected = currentScreen == BottomNavScreen.ReceivedRequestScreen.route,
                                    onClick = {
                                        currentScreen = BottomNavScreen.ReceivedRequestScreen.route
                                        navHostController.navigate(BottomNavScreen.ReceivedRequestScreen.route)
                                    },
                                    icon = {
                                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                                    },
                                    label = {
                                        Text(
                                            text = "Requests")
                                    })
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            BottomNavGraph(
                                navHostController = navHostController,
                                onGoToSendRequestActivity = {
                                    Log.d("test2",it.toString())
                                    val intent = Intent(this@HomeActivity,SendRequestActivity::class.java).apply {
                                        putExtra("notification",it)
                                    }
                                    startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

