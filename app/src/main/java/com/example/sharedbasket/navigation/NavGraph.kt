package com.example.sharedbasket.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sharedbasket.ui.goToMarketScreen.GoToMarketScreen
import com.example.sharedbasket.ui.registerScreen.RegisterScreen
import com.example.sharedbasket.ui.loginScreen.LoginScreen
import com.example.sharedbasket.ui.mapScreen.MapScreen
import com.example.sharedbasket.ui.notificationScreen.NotificationScreen
import com.example.sharedbasket.ui.receivedRequestScreen.ReceivedRequestScreen
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.ReceivedRequestState
import com.google.android.gms.maps.model.LatLng

@Composable
fun NavGraph(
    navHostController: NavHostController,
    activity: Activity,
    onNavigateToMapActivity : () -> Unit,
    onNavigateToHomeScreen : () -> Unit
){
    NavHost(navController = navHostController, startDestination = SharedBasketScreen.LoginScreen.route){
        composable(route = SharedBasketScreen.LoginScreen.route){
            LoginScreen(activity = activity,
                onNavigateToRegisterScreen = { navHostController.navigate(SharedBasketScreen.RegisterScreen.route) },
                onNavigateToHomeScreen = onNavigateToHomeScreen
            )
        }
        composable(route = SharedBasketScreen.RegisterScreen.route){
            RegisterScreen(activity = activity, onNavigateToMapActivity = onNavigateToMapActivity)
        }
        composable(route = SharedBasketScreen.MapScreen.route){
            MapScreen()
        }
    }
}

@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    startingScreen : String,
    onGoToSendRequestActivity : (Notification) -> Unit,
    onGoToReceivedRequestDetailActivity : (ReceivedRequestState) -> Unit
) {
    NavHost(navController = navHostController, startDestination = startingScreen){
        composable(route = BottomNavScreen.NotificationScreen.route){
            NotificationScreen(onGoToSendRequestActivity = {
                onGoToSendRequestActivity(it)
            })
        }
        composable(route = BottomNavScreen.GoToMarketScreen.route){
            GoToMarketScreen()
        }
        composable(route = BottomNavScreen.ReceivedRequestScreen.route){
            ReceivedRequestScreen(
                onGoToReceivedRequestDetailActivity = onGoToReceivedRequestDetailActivity
            )
        }
    }
}