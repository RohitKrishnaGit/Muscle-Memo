package com.cs346.musclememo.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel
import com.cs346.musclememo.SocketManager
import com.cs346.musclememo.navigation.Screen
import com.cs346.musclememo.utils.AppPreferences


//TEMPORARILY USED AS EXAMPLE TO HELP FRONT END DEVS, REMOVE FOR FINAL
@Composable
fun ChatTestScreen() {
    val viewModel = viewModel<LeaderboardScreenViewModel>()
    val sm by remember{ mutableStateOf(SocketManager()) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LaunchedEffect(Unit) {
            sm.connect()
            sm.joinRoom("3", AppPreferences.refreshToken.toString())
            sm.onMessageReceived { msg -> println(msg) }
            sm.onHistoryRequest { msg -> println(msg) }
        }
        MMButton(
            onClick = {
                if (AppPreferences.refreshToken!=null) {
                    sm.sendMessage("Hello, everyone!")
                }
            },
            text = "Send Message Test",
            maxWidth = true
        )
        //need to sm.disconnect() on navigation away from chat
    }
}