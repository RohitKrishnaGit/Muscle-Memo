package com.cs346.musclememo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.viewmodels.FindBuddyViewModel

@Composable
fun FindBuddyScreen(
    viewModel: FindBuddyViewModel = viewModel<FindBuddyViewModel>()
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Find a Buddy Screen"
        )
    }
}