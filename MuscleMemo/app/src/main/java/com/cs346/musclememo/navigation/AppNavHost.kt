package com.cs346.musclememo.navigation

import android.graphics.Paint.Join
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs346.musclememo.screens.FriendsScreen
import com.cs346.musclememo.screens.JoinWorkoutScreen
import com.cs346.musclememo.screens.LeaderboardScreen
import com.cs346.musclememo.screens.LoginScreen
import com.cs346.musclememo.screens.ProfileScreen
import com.cs346.musclememo.screens.WorkoutScreen
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun AppNavHost (
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    startDestination: String,
    selected: MutableIntState
) {
    val workoutScreenViewModel = viewModel<WorkoutScreenViewModel>()

    NavHost (
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            selected.intValue = 2
            LoginScreen(onSuccessLogin = {
                workoutScreenViewModel.resetState()
                navController.navigate(NavItem.Workout.screen.route) { popUpTo(navController.graph.id) {inclusive = true} }
                bottomBarState.value = true
            })
        }
        composable(route = Screen.Profile.route) {
            selected.intValue = 4
            ProfileScreen( signOut = {
                bottomBarState.value = false
                navController.navigate(Screen.Login.route) { popUpTo(navController.graph.id) {inclusive = true} }
                AppPreferences.refreshToken = null
                AppPreferences.accessToken = null
            })
        }
        composable(route = Screen.Leaderboard.route) {
            selected.intValue = 3
            LeaderboardScreen()
        }
        composable(route = Screen.Friends.route) {
            selected.intValue = 0
            FriendsScreen(bottomBarState = bottomBarState)
        }
        composable(route = Screen.Workout.route) {
            selected.intValue = 2
            WorkoutScreen(viewModel = workoutScreenViewModel)
        }
        composable(route = Screen.JoinWorkout.route) {
            selected.intValue = 1
            JoinWorkoutScreen(bottomBarState = bottomBarState)
        }
    }
}

