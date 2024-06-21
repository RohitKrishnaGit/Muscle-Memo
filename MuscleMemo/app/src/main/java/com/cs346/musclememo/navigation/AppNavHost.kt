package com.cs346.musclememo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs346.musclememo.screens.LoginScreenContent

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
    }
}