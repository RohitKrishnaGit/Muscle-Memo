package com.cs346.musclememo.navigation

sealed class Screen (val route: String){
    object Login : Screen(route = "login")
    object Workout : Screen(route = "workout")
    object Leaderboard : Screen(route = "leaderboard")
    object Profile : Screen(route = "profile")
    object FindBuddy : Screen(route = "findBuddy")
    object Friends : Screen(route = "friend")
}