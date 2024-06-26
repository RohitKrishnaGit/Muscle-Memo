package com.cs346.musclememo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.R
import com.cs346.musclememo.screens.viewmodels.LoginScreenViewModel


@Composable
fun LoginScreen(
    onClick: () -> Unit
) {
    val viewModel = viewModel<LoginScreenViewModel>()
    var loginErrorMessage by remember { mutableStateOf("") }
    var username by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.musclememo),
            contentDescription = "Login image",
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp))
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

       //Username
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Login to your Account")
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

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = {
            Text(text = "Password")
        },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer (modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.loginAttempt(username, password, {
                onClick()
            }) {
                password = ""
                loginErrorMessage = "Invalid Credentials. Please try again."
            }
        }){
            Text(text="Login")
        }

        //Invalid Login
        Text(loginErrorMessage)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Create Account", modifier = Modifier.clickable{

        })

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Forgot Password?", modifier = Modifier.clickable {

        })

    }


}