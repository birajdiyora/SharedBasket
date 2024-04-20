package com.example.sharedbasket.navigation

sealed class SharedBasketScreen(
    val title : String,
    val route : String
){
    object LoginScreen : SharedBasketScreen(title = "Login", route = "login_screen")
    object RegisterScreen : SharedBasketScreen(title = "register", route = "register_screen")
    object MapScreen : SharedBasketScreen(title = "map", route = "map_screen")
}

sealed class BottomNavScreen(
    val title : String,
    val route : String
){
    object NotificationScreen : BottomNavScreen(title = "Notification", route = "notification_screen")
    object GoToMarketScreen : BottomNavScreen(title = "GoToMarket", route = "gotomarket_screen")
    object ProfileScreen : BottomNavScreen(title = "Profile", route = "profile_screen")
}
