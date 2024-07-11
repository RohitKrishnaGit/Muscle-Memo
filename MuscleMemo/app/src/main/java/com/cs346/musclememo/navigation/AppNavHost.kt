package com.cs346.musclememo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs346.musclememo.screens.FriendsScreen
import com.cs346.musclememo.screens.HistoryScreen
import com.cs346.musclememo.screens.LeaderboardScreen
import com.cs346.musclememo.screens.LoginScreen
import com.cs346.musclememo.screens.ProfileScreen
import com.cs346.musclememo.screens.WorkoutScreen
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun AppNavHost (
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    startDestination: String,
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
            ProfileScreen()
        }
        composable(route = Screen.Leaderboard.route) {
            LeaderboardScreen()
        }
        composable(route = Screen.Friends.route) {
            FriendsScreen()
        }
        composable(route = Screen.Workout.route) {
            WorkoutScreen()
        }
        composable(route = Screen.History.route) {
            HistoryScreen()
        }
    }
}