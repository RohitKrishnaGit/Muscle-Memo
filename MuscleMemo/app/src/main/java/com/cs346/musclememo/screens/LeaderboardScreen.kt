package com.cs346.musclememo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LeaderboardScreenContent(
    //viewModel: LeaderboardScreenViewModel = hiltViewModel()
){
    LeaderboardScreen(
        //state = state,
    )
}

@Composable
private fun LeaderboardScreen(
    //state:
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Leaderboard Screen"
        )
    }
}