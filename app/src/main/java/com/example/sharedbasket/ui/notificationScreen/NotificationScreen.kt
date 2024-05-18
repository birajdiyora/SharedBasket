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
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.common.DataIsEmpty
import com.example.sharedbasket.navigation.BottomNavScreen
import com.example.sharedbasket.timestampToDateTimeString
import com.example.sharedbasket.utils.Notification

@OptIn(ExperimentalMaterial3Api::class)
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
        if(viewModel.isDataReceived){
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }else {
            if(notificationListState.notificationList.isEmpty()){
                DataIsEmpty()
            }else {
                LazyColumn() {
                    items(notificationListState.notificationList.reversed()) { notification ->
                        NotificationCard(notification, onGoToSendRequestActivity)
                    }
                }
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
            .height(100.dp)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        Text(
//                            text = "Status",
//                            fontSize = 14.sp
//                        )
                        Text(
                            text = if(notification.status.equals("pending")){
                                "Pending"
                                }else if(notification.status.equals("confirm")){
                                  "Confirm"
                                 }else if(notification.status.equals("delivered")){
                                    "Delivered"
                                 }else{
                                      ""
                                      }
                                ,
                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFFFA000)
//                            color = Color(0xFF32AC38)
                            color = if(notification.status.equals("pending")){
                                Color.Black
                            }else if (notification.status.equals("confirm")){
                                Color(0xFFFF9930)
                            }else if(notification.status.equals("delivered")){
                                Color(0xFF32AC38)
                            }else{
                                Color.Transparent
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {  onGoToSendRequestActivity(notification) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White)
                    }
                }
//                Button(
//                    onClick = {
//                        onGoToSendRequestActivity(notification)
//                              },
//                    modifier = Modifier
//                        .height(40.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor =  if(notification.status.equals("pending")){
//                            Color(0xFFFFA000)
//                        }else if(notification.status.equals("confirm")){
//                                Color(0xFFFFA000)
//                        }else{
//                            MaterialTheme.colorScheme.primary
//                        }
//                    )
//                ) {
//                    Text(
//                        text = if(notification.status.equals("pending")){
//                            "Pending"
//                        } else if(notification.status.equals("confirm")){
//                            "Confirm"
//                        }else{
//                             "Send request"
//                             },
//                        fontSize = 11.sp
//                    )
//                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${timestampToDateTimeString(notification.timeStamp)}",
                    fontSize = 12.sp
                )
            }
        }
    }
}