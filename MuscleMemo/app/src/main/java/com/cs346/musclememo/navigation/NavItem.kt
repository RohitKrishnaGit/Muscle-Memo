package com.cs346.musclememo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    var name: String,
    var icon: ImageVector,
    val screen: Screen
){
    object Leaderboard : NavItem("Rankings", Icons.Default.Leaderboard, Screen.Leaderboard)
    object Friend : NavItem("Friends", Icons.Default.Favorite, Screen.Friends)
    object Workout : NavItem("Workout", Icons.Default.Add, Screen.Workout)
    object History : NavItem("History", Icons.Default.AccessTime, Screen.History)
    object Profile : NavItem("Profile", Icons.Default.AccountCircle, Screen.Profile)
}

