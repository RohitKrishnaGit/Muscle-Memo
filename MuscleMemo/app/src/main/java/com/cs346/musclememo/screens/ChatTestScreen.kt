package com.cs346.musclememo.screens

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
            sm.joinRoom(3)
            sm.onMessageReceived { msg -> println(msg) }
        }
        MMButton(
            onClick = {
                sm.sendMessage("Hello, everyone!")

                Thread.sleep(3000) // Allow time to receive messages

                //sm.disconnect()
            },
            text = "Start A New Workout",
            maxWidth = true
        )
    }
}