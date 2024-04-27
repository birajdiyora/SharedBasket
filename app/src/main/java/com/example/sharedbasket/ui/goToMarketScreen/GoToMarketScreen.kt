package com.example.sharedbasket.ui.goToMarketScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.common.CommonDialog
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.utils.ResultState
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoToMarketScreen(
    viewModel: GoToMarketScreenViewModel = hiltViewModel()
) {
    var marketName by remember {
        mutableStateOf("")
    }
    var isDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    if(isDialog)
        CommonDialog()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFFE9E7E3)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "You Are Going To Market!!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = marketName,
                    onValueChange = {
                                    marketName = it
                    },
                    label = {
                        Text(text = "Enter Market Name")
                    },
                    colors = TextFieldDefaults.colors(
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    scope.launch {
                        Log.d("test1",FirebaseMessaging.getInstance().token.await())
                    }
                    if(!marketName.equals("")) {
                        scope.launch {
                            viewModel.sendNotificationToAll(marketName = marketName).collect {
                                when (it) {
                                    is ResultState.Loading -> {
                                        isDialog = true
                                    }

                                    is ResultState.Success -> {
                                        isDialog = false
                                        "Notifications Send Successfully".toast()
                                        marketName = ""
                                    }

                                    is ResultState.Failure -> {
                                        isDialog = false
                                        "Failed".toast()
                                    }
                                }
                            }
                        }
                    }else{
                        "Enter Market Name".toast()
                    }

                }) {
                    Text(text = "Send notification")
                }
            }
        }
    }
}