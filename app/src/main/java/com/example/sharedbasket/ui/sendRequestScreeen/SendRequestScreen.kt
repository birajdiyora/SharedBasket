package com.example.sharedbasket.ui.sendRequestScreeen

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.utils.Item
import com.example.sharedbasket.utils.Notification
import com.example.sharedbasket.utils.ResultState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendRequestScreen(
    notification: Notification,
    viewModel: SendRequestViewModel = hiltViewModel(),
    onGoToHomeActivity : () -> Unit
) {
    val items = remember { mutableStateListOf<Item>(Item("","","")) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val itemListState by viewModel.itemListState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
//                            Text(
//                                text = "Send Request To ",
//                            )
                            Text(
                                text = " ${notification.marketerName}",
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                },
                    navigationIcon = {
                        IconButton(onClick = {
                            onGoToHomeActivity()
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    if(notification.status.equals("")) {
                        OutlinedButton(
                            onClick = {
                                Log.d("test1", items.toList().toString())
                                scope.launch {
                                    viewModel.insertItemRequestData(
                                        notification = notification,
                                        items = items.toList()
                                    ).collect {
                                        when (it) {
                                            is ResultState.Loading -> {
//                                                   "Loading".toast()
                                            }

                                            is ResultState.Success -> {
                                                "Request send Successfully".toast()
                                                onGoToHomeActivity()
                                            }

                                            is ResultState.Failure -> {
                                                "Failed To Send request".toast()
                                            }
                                            else ->{

                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Submit",
                                fontSize = 17.sp
                            )
                        }
                    }else if(notification.status.equals("confirm")){
                        OutlinedButton(
                            onClick = {
//                                Log.d("test1", items.toList().toString())
                                scope.launch {
                                    viewModel.updateItemStatus("delivered")
                                    onGoToHomeActivity()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Delivered",
                                fontSize = 17.sp
                            )
                        }
                    }else if(notification.status.equals("delivered")){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "All Items Are delivered",
                                color = Color(0xFF32AC38),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        ) {
            if(notification.status.equals("pending") || notification.status.equals("confirm") || notification.status.equals("delivered")) {
                Column(
                    modifier = Modifier
                        .padding(it)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(itemListState.itemList) {
                            ListItemCard(it)
                        }
                    }
                }
            }else if(notification.status.equals("")){
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(state = scrollState)
                ) {
                    Log.d("test", "status is blank ${notification.status}")
                    items.forEachIndexed { index, item ->
                        AddItemCard(index, item, items)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FloatingActionButton(onClick = {
                            items.add(Item("", ""))
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }else{
//                Column(
//                    modifier = Modifier
//                        .padding(it)
//                ) {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        items(itemListState.itemList) {
//                            ListItemCard(it)
//                        }
//                    }
//                }
            }
        }
    }
}

@Composable
fun AddItemCard(index: Int, item: Item, items: MutableList<Item>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Item No ${index+1}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = item.itemName,
                onValueChange = {
                    items[index] = item.copy(itemName = it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                label = {
                    Text(text = "Enter Item Name")
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = item.itemDescription,
                onValueChange = {
                    items[index] = item.copy(itemDescription = it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                label = {
                    Text(text = "Enter Item description")
                }
            )
        }
    }
}

@Composable
fun ListItemCard(
    item: Item
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "${item.itemName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${item.itemDescription}",
                fontSize = 15.sp
                )
            if(!item.itemPrice.equals("")){
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.itemPrice}",
                    fontSize = 15.sp
                )
            }
        }
    }
}