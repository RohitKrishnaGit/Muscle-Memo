package com.cs346.musclememo.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar (
    bottomBarState: MutableState<Boolean>,
    navHostController: NavHostController
) {
    val items = mutableMapOf(
        NavItem.Leaderboard to false,
        NavItem.Friend to false,
        NavItem.Workout to false,
        NavItem.History to false,
        NavItem.Profile to false
    )
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomAppBar(
                contentPadding = PaddingValues(0.dp)
            ) {
                // todo: fix NavBarItem highlighting
                Row {
                    items.forEach { item ->
                        NavBarItem(key = item.key, value = item.value, modifier = Modifier.weight(1f)) {
                            navHostController.navigate(item.key.screen.route)
                        }
                        }
                    }
                }
    })
}


@Composable
fun NavBarItem (
    key: NavItem,
    value: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .background(
                color = if (value)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceContainerHigh
            ).clickable { onClick() }
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(key.icon, key.name)
                Text(text = key.name, fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}