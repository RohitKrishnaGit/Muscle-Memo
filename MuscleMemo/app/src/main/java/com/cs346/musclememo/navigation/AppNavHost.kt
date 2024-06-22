package com.cs346.musclememo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs346.musclememo.screens.FriendsScreenContent
import com.cs346.musclememo.screens.HistoryScreenContent
import com.cs346.musclememo.screens.LeaderboardScreenContent
import com.cs346.musclememo.screens.LoginScreenContent
import com.cs346.musclememo.screens.ProfileScreenContent
import com.cs346.musclememo.screens.WorkoutScreenContent

@Composable
fun AppNavHost (
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost (
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreenContent()
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
            WorkoutScreenContent()
        }
        composable(route = Screen.History.route) {
            HistoryScreenContent()
        }
    }
}