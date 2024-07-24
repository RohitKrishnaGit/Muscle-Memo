package com.cs346.musclememo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.ChooseLeaderboardExercise
import com.cs346.musclememo.screens.components.ChooseLeaderboardType
import com.cs346.musclememo.screens.components.DisplayLeaderboardExercise
import com.cs346.musclememo.screens.components.LeaderboardHeading
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardScreenViewModel = viewModel<LeaderboardScreenViewModel>()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ){
            Text(
                text = "Leaderboard",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DisplayLeaderboardExercise(viewModel = viewModel)
            Spacer(modifier = Modifier.height(10.dp))
            ChooseLeaderboardType(viewModel = viewModel)
            Spacer(modifier = Modifier.height(10.dp))
            Leaderboard(viewModel = viewModel)
        }
    }

    ChooseLeaderboardExercise(viewModel = viewModel)
}


@Composable
fun Leaderboard (
    viewModel: LeaderboardScreenViewModel
){
    val leaderboard = viewModel.leaderboardEntries
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            LeaderboardHeading()
        }
        itemsIndexed(items = leaderboard) { index, item ->
            if (item != null){
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = item.username,
                        fontSize = 16.sp,
                        color = if (item.username == viewModel.user.username) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = item.value,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}