package com.cs346.musclememo.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar (
    navHostController: NavHostController
) {
    val items = listOf(
        NavItem.Leaderboard,
        NavItem.Friend,
        NavItem.Workout,
        NavItem.History,
        NavItem.Profile
    )

    BottomAppBar {
        Row {
            items.forEach { item ->
                Button(
                    shape = RectangleShape,
                    onClick = { navHostController.navigate(item.screen.route) },
                    modifier = Modifier.weight(0.2f)
                ) {
                    Column (modifier = Modifier.fillMaxSize()){
                        Icon(item.icon, item.name)
                        Text(text = item.name)
                    }
                }
            }
        }
    }
}