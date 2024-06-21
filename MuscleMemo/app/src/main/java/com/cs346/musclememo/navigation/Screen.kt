package com.cs346.musclememo.navigation

sealed class Screen (val route: String){
    object Login : Screen(route = "main")
    object Workout : Screen(route = "login")
    object Leaderboard : Screen(route = "leaderboard")
    object Profile : Screen(route = "profile")
    object History : Screen(route = "history")
    object Friends : Screen(route = "friend")
}