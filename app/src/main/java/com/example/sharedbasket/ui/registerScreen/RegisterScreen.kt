package com.example.sharedbasket.ui.registerScreen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharedbasket.LoginActivity
import com.example.sharedbasket.MapActivity
import com.example.sharedbasket.common.CommonDialog
import com.example.sharedbasket.common.OtpView
import com.example.sharedbasket.common.toast
import com.example.sharedbasket.navigation.SharedBasketScreen
import com.example.sharedbasket.ui.loginScreen.LoginScreenViewModel
import com.example.sharedbasket.utils.ResultExist
import com.example.sharedbasket.utils.ResultState
import com.google.firebase.messaging.FirebaseMessaging
import com.google.maps.android.compose.GoogleMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    loginScreenViewModel: LoginScreenViewModel = hiltViewModel(),
    registerScreenViewModel: RegisterScreenViewModel = hiltViewModel(),
    onNavigateToMapActivity : ()-> Unit
) {
    var otp by remember {
        mutableStateOf("")
    }
    var mobileNo by remember {
        mutableStateOf("")
    }
    var name by remember {
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
            text = "Register",
            fontSize = 60.sp,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (!isOtpSend) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Enter Name",
                        fontWeight = FontWeight.Bold
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
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
                    if (mobileNo.length < 10 || name.length==0) {
                        "Enter Proper detail".toast()
                    } else {
                        scope.launch(Dispatchers.Main) {
                            loginScreenViewModel.isUserAlreadyRegister(mobileNo = mobileNo).collect{
                                if(it != ResultExist.Exist) {
                                    loginScreenViewModel.createUserWithPhone(
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
                                            }

                                            is ResultState.Loading -> {
                                                isDialog = true
                                            }
                                        }
                                    }
                                }else{
                                    "User is Already register".toast()
                                }                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Send OTP")
            }
        }else{
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
                        val FCMToken = FirebaseMessaging.getInstance().token.await()

                        loginScreenViewModel.signInWithCredential(
                            code = otp
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isDialog = false
//                                    "Login Successfuly".toast()
                                    LoginActivity.userManager.storeUserMobileNumber(mobileNo)
                                    LoginActivity.userManager.storeUserName(name)
                                    LoginActivity.userManager.storeFCmKey(FCMToken)
                                    onNavigateToMapActivity()
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
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Register")
            }
        }
    }
}
