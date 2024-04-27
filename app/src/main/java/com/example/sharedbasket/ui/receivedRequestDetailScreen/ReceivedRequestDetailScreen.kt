package com.example.sharedbasket.ui.receivedRequestDetailScreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.HomeActivity
import com.example.sharedbasket.utils.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedRequestDetailScreen(
    viewModel: ReceivedRequestDetailScreenViewModel = hiltViewModel(),
    onGoToHomeActivity: () -> Unit
) {
    val items = remember { mutableStateListOf<Item>() }
    items.addAll(viewModel.receivedRequestState!!.items)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Update Items Price",
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
                OutlinedButton(
                    onClick = {
                              Log.d("test6",items.toList().toString())
                        viewModel.updateItemPrice(items)
                        onGoToHomeActivity()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Accept Request And Update Price")
                }
            }
        }
    ) {
        val scrollState = rememberScrollState()
//        val items = remember { mutableStateListOf<Item>() }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            items.forEachIndexed { index, item ->
//                items[index] = item
                displayItem(item = item, index = index,items = items)
            }
//           LazyColumn {
////               itemsIndexed(viewModel.receivedRequestState!!.items){
////                    displayItem(item = it)
////               }
//               itemsIndexed(listOf(1)){
//
//               }
//           }
        }
    }
}

@Composable
fun displayItem(
    item: Item,
    index : Int,
    items : MutableList<Item>
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
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = item.itemPrice,
                onValueChange = {
                                items[index] = item.copy(itemPrice = it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(text = "Enter Price")
                }
            )
        }
    }
}