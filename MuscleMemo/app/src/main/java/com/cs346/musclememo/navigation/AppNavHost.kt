package com.cs346.musclememo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs346.musclememo.screens.ChatTestScreen
import com.cs346.musclememo.screens.FindBuddyScreen
import com.cs346.musclememo.screens.FriendsScreen
import com.cs346.musclememo.screens.LeaderboardScreen
import com.cs346.musclememo.screens.LoginScreen
import com.cs346.musclememo.screens.ProfileScreen
import com.cs346.musclememo.screens.WorkoutScreen
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun AppNavHost (
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    startDestination: String,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost (
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(onSuccessLogin = {
                navController.navigate(NavItem.Workout.screen.route) { popUpTo(navController.graph.id) {inclusive = true} }
                bottomBarState.value = true
            }
            )
            //ChatTestScreen()
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen( signOut = {
                bottomBarState.value = false
                navController.navigate(Screen.Login.route) { popUpTo(navController.graph.id) {inclusive = true} }
                AppPreferences.refreshToken = null
                AppPreferences.accessToken = null
            })
        }
        composable(route = Screen.Leaderboard.route) {
            LeaderboardScreen()
        }
        composable(route = Screen.Friends.route) {
            FriendsScreen()
        }
        composable(route = Screen.Workout.route) {
            WorkoutScreen(viewModel = viewModel<WorkoutScreenViewModel>(viewModelStoreOwner))
        }
        composable(route = Screen.FindBuddy.route) {
            FindBuddyScreen()
        }
    }
}

