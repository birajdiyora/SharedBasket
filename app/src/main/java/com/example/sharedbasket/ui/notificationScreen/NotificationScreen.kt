package com.example.sharedbasket.ui.notificationScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.navigation.BottomNavScreen
import com.example.sharedbasket.timestampToDateTimeString
import com.example.sharedbasket.utils.Notification

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationScreenViewModel = hiltViewModel(),
    onGoToSendRequestActivity : (Notification) -> Unit
) {
    val notificationListState by viewModel.notificationListState.collectAsState()
    val requestState by viewModel.requestState.collectAsState()
//    Text(text = "Notification Screen")
//    Log.d("test","in screen ${timestampToDateTimeString(notificationListState.notificationList[0].timeStamp)}")
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Log.d("test1",notificationListState.notificationList.toString())
        LazyColumn(){
            items(notificationListState.notificationList.reversed()){notification ->

                NotificationCard(notification,onGoToSendRequestActivity)
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    onGoToSendRequestActivity : (Notification) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(90.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Text(
                    text = "${notification.marketerName}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                ) {
                    Text(
                        text = "I am Going To ",
                        fontSize = 13.sp)
                    Text(
                        text = "${notification.marketName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onGoToSendRequestActivity(notification)
                              },
                    modifier = Modifier
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =  if(notification.status.equals("pending")){
                            Color(0xFFFFA000)
                        }else if(notification.status.equals("confirm")){
                                Color(0xFFFFA000)
                        }else{
                            MaterialTheme.colorScheme.primary
                        }
                    )
                ) {
                    Text(
                        text = if(notification.status.equals("pending")){
                            "Pending"
                        } else if(notification.status.equals("confirm")){
                            "Confirm"
                        }else{
                             "Send request"
                             },
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${timestampToDateTimeString(notification.timeStamp)}",
                    fontSize = 12.sp
                )
            }
        }
    }
}