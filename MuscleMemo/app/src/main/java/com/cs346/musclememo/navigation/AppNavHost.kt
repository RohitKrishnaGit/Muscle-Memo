package com.cs346.musclememo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs346.musclememo.screens.FriendsScreenContent
import com.cs346.musclememo.screens.HistoryScreenContent
import com.cs346.musclememo.screens.LeaderboardScreenContent
import com.cs346.musclememo.screens.LoginScreen
import com.cs346.musclememo.screens.ProfileScreenContent
import com.cs346.musclememo.screens.WorkoutScreen

@Composable
fun AppNavHost (
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    startDestination: String = Screen.Login.route
) {
    NavHost (
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(onClick = {
                navController.navigate(NavItem.Workout.screen.route)
                bottomBarState.value = true
            })
        }
        composable(route = Screen.Profile.route) {
            ProfileScreenContent()
        }
        composable(route = Screen.Leaderboard.route) {
            LeaderboardScreenContent()
        }
        composable(route = Screen.Friends.route) {
            FriendsScreenContent()
        }
        composable(route = Screen.Workout.route) {
            WorkoutScreen()
        }
        composable(route = Screen.History.route) {
            HistoryScreenContent()
        }
    }
}