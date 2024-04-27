package com.example.sharedbasket.ui.receivedRequestScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.timestampToDateTimeString
import com.example.sharedbasket.utils.ReceivedRequestState

@Composable
fun ReceivedRequestScreen(
    viewModel: ReceivedRequestScreenViewModel = hiltViewModel(),
    onGoToReceivedRequestDetailActivity : (ReceivedRequestState) -> Unit
) {
    val receivedRequestState by viewModel.receivedRequestStateList.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
//        Log.d("tes6",receivedRequestState.list.toString())
        if(receivedRequestState.list.isEmpty()){
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }else {
            LazyColumn {
                items(receivedRequestState.list.reversed()) {
                    ReceivedRequestCard(
                        receivedRequestState = it,
                        onGoToReceivedRequestDetailActivity = onGoToReceivedRequestDetailActivity
                    )
                }
            }
        }
    }
}
@Composable
fun ReceivedRequestCard(
    receivedRequestState: ReceivedRequestState,
    onGoToReceivedRequestDetailActivity : (ReceivedRequestState) -> Unit
) {
//    Log.w("test",receivedRequestState.toString())
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
                    text = "${receivedRequestState.itemsRequesterName}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                ) {
                    Text(
                        text = "Requested for some items",
                        fontSize = 13.sp)
                    Text(
                        text = "",
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
                            text = if(receivedRequestState.status.equals("pending")){
                                "Pending"
                            }else if(receivedRequestState.status.equals("confirm")){
                                "Confirm"
                            }else if(receivedRequestState.status.equals("delivered")){
                                "Delivered"
                            }else{
                                ""
                            }
                            ,
                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFFFA000)
//                            color = Color(0xFF32AC38)
                            color = if(receivedRequestState.status.equals("pending")){
                                Color.Black
                            }else if (receivedRequestState.status.equals("confirm")){
                                Color(0xFFFF9930)
                            }else if(receivedRequestState.status.equals("delivered")){
                                Color(0xFF32AC38)
                            }else{
                                Color.Transparent
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onGoToReceivedRequestDetailActivity(receivedRequestState)  },
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
//                        onGoToReceivedRequestDetailActivity(receivedRequestState)
//                    },
//                    modifier = Modifier
//                        .height(40.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor =  if(receivedRequestState.status.equals("pending")){
//                            Color(0xFFFFA000)
//                        }else{
//                            MaterialTheme.colorScheme.primary
//                        }
//                    )
//                ) {
//                    Text(
//                        text = if(receivedRequestState.status.equals("pending")){
//                            "Pending"
//                        } else if (receivedRequestState.status.equals("confirm")){
//                            "Confirm"
//                        }else{
//                             "View"
//                             },
//                        fontSize = 11.sp
//                    )
//                }
            }
        }
    }
}