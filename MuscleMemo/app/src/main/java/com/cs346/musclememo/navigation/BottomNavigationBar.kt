package com.cs346.musclememo.navigation

import android.graphics.fonts.FontStyle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomNavigationBar (
    bottomBarState: Boolean,
    navHostController: NavHostController,
    selected: MutableIntState
) {
    val items = listOf(
        NavItem.Friend,
        NavItem.JoinWorkout,
        NavItem.Workout,
        NavItem.Leaderboard,
        NavItem.Profile
    )
    LaunchedEffect (bottomBarState) {
        if (bottomBarState)
            selected.intValue = items.indexOf(items.find {it.screen.route == navHostController.currentDestination?.route})
    }
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomAppBar(
                contentPadding = PaddingValues(0.dp)
            ) {
                Row {
                    items.forEachIndexed { index, item ->
                        NavBarItem(
                            value = item,
                            highlighted = (index == selected.intValue),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (index != selected.intValue) {
                                navHostController.navigate(item.screen.route)
                            }
                        }
                    }
                }
            }
        })
}


@Composable
fun NavBarItem (
    value: NavItem,
    highlighted: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(value.icon, null, tint = if (highlighted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value.name, fontSize = 10.sp, fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal, color = if (highlighted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}