package com.cs346.musclememo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cs346.musclememo.navigation.NavItem

@Composable
fun LoginScreenContent(
    //viewModel: LoginScreenViewModel = hiltViewModel()
    navController: NavHostController
){
    LoginScreen(
        navController
    )
}

@Composable
private fun LoginScreen(
    navHostController: NavHostController

) {

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
//        Image(painter = painterResource(id = R.drawable.musclememo),
//            contentDescription = "Login image",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp))
        Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

       //Username
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Login to your Account")
        OutlinedTextField(value = username, onValueChange = {
            username = it
        }, label = {
            Text(text = "Username")
        },)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = {
            Text(text = "Password")
        })

        Spacer (modifier = Modifier.height(16.dp))
        Button(onClick = {
            navHostController.navigate(NavItem.Workout.screen.route)
        }){
            Text(text="Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Create Account", modifier = Modifier.clickable{

        })

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Forgot Password?", modifier = Modifier.clickable {

        })

    }


}