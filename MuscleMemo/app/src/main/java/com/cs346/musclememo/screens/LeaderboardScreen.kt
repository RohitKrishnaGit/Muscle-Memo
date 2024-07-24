package com.cs346.musclememo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.ChooseLeaderboardExercise
import com.cs346.musclememo.screens.components.ChooseLeaderboardType
import com.cs346.musclememo.screens.components.DisplayLeaderboardExercise
import com.cs346.musclememo.screens.components.LeaderboardHeading
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel
import com.cs346.musclememo.utils.AppPreferences

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
                fontSize = 40.sp
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
    FirstTimeDialog(viewModel)
}

@Composable
fun FirstTimeDialog(
    viewModel: LeaderboardScreenViewModel
){
    MMDialog(
        showDialog = viewModel.showFirstTimeDialog ?: false,
        title = "Welcome!",
        onConfirm = {
            viewModel.updateShowFirstTimeDialog(false) },
        onDismissRequest = {  viewModel.updateShowFirstTimeDialog(false) },
        body = {
            Text(text = "This is a leaderboard to show off your Personal Records! Please note the scores are unmoderated and are just to be used for fun. By default, your scores are hidden from others.\n\nIf you see this icon:")
            Spacer(modifier = Modifier.height(5.dp))
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Filled.Visibility, null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "or")
                Spacer(modifier = Modifier.width(5.dp))
                Icon(Icons.Filled.VisibilityOff, null)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "it toggles the visibility of your PR.\n\nPlease workout responsibly and have fun!")
        },
        dismiss = false
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Leaderboard (
    viewModel: LeaderboardScreenViewModel
){
    val leaderboard = viewModel.leaderboardEntries
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = viewModel.isLeaderboardRefreshing,
        state = state,
        onRefresh = {
            viewModel.fetchAllLeaderboards()
        }
    )
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                LeaderboardHeading()
            }
            itemsIndexed(items = leaderboard) { index, item ->
                if (item != null) {
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
}