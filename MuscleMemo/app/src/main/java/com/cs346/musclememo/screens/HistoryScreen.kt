package com.cs346.musclememo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.WorkoutHistoryCard
import com.cs346.musclememo.screens.viewmodels.HistoryScreenViewModel

@Composable
fun HistoryScreen() {
    val viewModel = viewModel<HistoryScreenViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){
        Column {
            Text(text = "History Screen", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            val listState = rememberLazyListState()
            LazyColumn (
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewModel.user?.let {
                    items(items = it.workouts){ workout ->
                        WorkoutHistoryCard(workout = workout)
                    }
                }
            }
        }
    }
}