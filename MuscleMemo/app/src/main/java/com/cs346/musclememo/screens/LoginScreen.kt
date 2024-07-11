package com.cs346.musclememo.screens

import android.renderscript.ScriptGroup.Input
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.R
import com.cs346.musclememo.screens.components.InputSheet
import com.cs346.musclememo.screens.components.PasswordTextField
import com.cs346.musclememo.screens.viewmodels.LoginScreenViewModel
import kotlin.math.log


@Composable
fun LoginScreen(
    onSuccessLogin: () -> Unit
) {
    val viewModel = viewModel<LoginScreenViewModel>()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.musclememo),
            contentDescription = "Login image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Text(text = "Track your progress.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Find friends.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Improve.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Button(
            onClick = { viewModel.setSignupScreenVisible(true) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Sign up free", fontSize = 16.sp)
        }
        Button(
            onClick = { viewModel.setLoginScreenVisible(true) },
            colors = ButtonDefaults.outlinedButtonColors(),
            modifier = Modifier.fillMaxWidth(0.7f)) {
            Text(text = "Log in", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
    LoginAccountSheet(viewModel = viewModel, onSuccessLogin = onSuccessLogin)
    CreateAccountSheet(viewModel = viewModel)
}

@Composable
fun LoginAccountSheet(
    viewModel: LoginScreenViewModel,
    onSuccessLogin: () -> Unit
){
    var loginErrorMessage by remember { mutableStateOf("") }
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    InputSheet(
        title = "Log in",
        visible = viewModel.loginVisible,
        setVisible = { viewModel.setLoginScreenVisible(it) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            //Username
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = {
                    Text(text = "Username")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(1f)
            )

            // Password
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(password = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(1f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Forgot Password?", modifier = Modifier.clickable {

            })
            // Login Button
            Spacer(modifier = Modifier.height(16.dp))
            Row (modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    viewModel.loginAttempt(username, password, {
                        onSuccessLogin()
                    }) {
                        password = ""
                        loginErrorMessage = "Invalid Credentials. Please try again."
                    }
                }) {
                    Text(text = "Log in", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            if (loginErrorMessage != "") {
                Spacer(modifier = Modifier.height(4.dp))
                //Invalid Login
                Row (modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(loginErrorMessage)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }


        }
    }
}


@Composable
fun CreateAccountSheet(
    viewModel: LoginScreenViewModel
) {
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordCheck by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var fullName by remember {
        mutableStateOf("")
    }

    var createErrorMessage by remember {
        mutableStateOf("")
    }

    InputSheet(
        title = "Sign up",
        visible = viewModel.signupVisible,
        setVisible = { viewModel.setSignupScreenVisible(it)}
    ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ){
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                        },
                        label = {
                            Text(text = "Full Name")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text(text = "Email")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        label = {
                            Text(text = "Username")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text(text = "Password")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = passwordCheck,
                        onValueChange = {
                            passwordCheck = it
                        },
                        label = {
                            Text(text = "Confirm Password")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    val errorMessage by remember { mutableStateOf("") }
                    if (errorMessage != ""){
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = createErrorMessage)
                    }
                    Button(onClick = {
                        if (password == passwordCheck){
                            viewModel.createAccountAttempt(username, password, email, fullName, {
                                viewModel.setSignupScreenVisible(false)
                                username = ""
                                email = ""
                                fullName = ""
                            }){ error ->
                                createErrorMessage = error
                            }
                        } else {
                            createErrorMessage = "Passwords do not match"
                        }
                        password = ""
                        passwordCheck = ""
                    }) {
                        Text(text = "Sign Up", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Already have an account?", modifier = Modifier.clickable {
                        viewModel.setSignupScreenVisible(false)
                        viewModel.setLoginScreenVisible(true)
                    })
                }
            }
        }
    }
