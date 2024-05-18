package com.example.sharedbasket.ui.loginScreen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.SharedBasketApplication
import com.example.sharedbasket.common.CommonDialog
import com.example.sharedbasket.common.OtpView
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.utils.ResultExist
import com.example.sharedbasket.utils.ResultState
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    viewModel: LoginScreenViewModel = hiltViewModel(),
    onNavigateToRegisterScreen : () -> Unit,
    onNavigateToHomeScreen : () -> Unit
) {
    var otp by remember {
        mutableStateOf("")
    }
    var mobileNo by remember {
        mutableStateOf("")
    }
    var isDialog by remember {
        mutableStateOf(false)
    }
    var isOtpSend by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    if(isDialog)
        CommonDialog()

    Column(
        modifier = modifier
            .padding(30.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 60.sp,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (!isOtpSend) {
            OutlinedTextField(
                value = mobileNo,
                onValueChange = {
                    if (it.length <= 10)
                        mobileNo = it
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(
                        text = "Enter Mobile Number",
                        fontWeight = FontWeight.Bold
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (mobileNo.length < 10) {
                        "Enter 10 Digit".toast()
                    } else {
                        isDialog = true
                        scope.launch(Dispatchers.Main) {
                            viewModel.isUserAlreadyRegister(mobileNo = mobileNo).collect{

                                if(it == ResultExist.Exist){
                                    viewModel.createUserWithPhone(
                                        mobile = mobileNo,
                                        activity = activity
                                    ).collect {
                                        when (it) {
                                            is ResultState.Success -> {
                                                isDialog = false
                                                "OTP send successfuly".toast()
                                                isOtpSend = true
                                            }

                                            is ResultState.Failure -> {
                                                isDialog = false
                                                "Some Error Accure".toast()
                                                Log.w("test",it.msg)
                                            }

                                            is ResultState.Loading -> {
                                                isDialog = true
                                            }
                                        }
                                    }
                                }else{
                                    "User Is Not Register".toast()
                                    isDialog = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Send OTP")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = "If You are not register click ",
                    fontSize = 15.sp
                )
                Text(
                    text = "Here",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier
                        .clickable {
                            onNavigateToRegisterScreen()
                        }
                )
            }
        } else {
            Row(
                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Enter OTP",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0EA70E)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OtpView(otpText = otp) {
                otp = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    scope.launch(Dispatchers.Main) {
                        viewModel.signInWithCredential(
                            code = otp
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isDialog = false
                                    "Login Successfully".toast()
                                    onNavigateToHomeScreen()

                                }

                                is ResultState.Failure -> {
                                    isDialog = false
                                    "Some Error Accure".toast()
                                }

                                is ResultState.Loading -> {
                                    isDialog = true
                                }
                            }
                        }
                    }
                    scope.launch {


                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}