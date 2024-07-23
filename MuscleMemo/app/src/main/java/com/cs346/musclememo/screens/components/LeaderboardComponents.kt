package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel

@Composable
fun LeaderboardHeading(){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Rank",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "Name",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "Score",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ChooseLeaderboardExercise(
    viewModel: LeaderboardScreenViewModel
){
    AnimatedVisibility(
        visible = viewModel.chooseExerciseVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {

    }
}

@Composable
fun DisplayLeaderboardExercise(
    viewModel: LeaderboardScreenViewModel
){
    val interactionSource = remember { MutableInteractionSource() }
    Row (
        modifier = Modifier.fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null, onClick = {
                viewModel.updateChooseExerciseVisible(true)
            })
    ) {
        OutlinedTextField(
            value = viewModel.currentExerciseRef.name,
            onValueChange = {},
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ChooseLeaderboardType(
    viewModel: LeaderboardScreenViewModel
){
    Row (
        modifier = Modifier.fillMaxWidth()
    ) {
        ChooseLeaderboardTypeButton(
            text = "Global",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.leaderboardType == "Global",
            onClick = {
                if (viewModel.leaderboardType != "Global")
                    viewModel.updateLeaderboardType("Global")
            }
        )
        ChooseLeaderboardTypeButton(
            text = "Friends",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.leaderboardType == "Friends",
            onClick = {
                if (viewModel.leaderboardType != "Friends")
                    viewModel.updateLeaderboardType("Friends")
            }
        )
    }
}

@Composable
fun ChooseLeaderboardTypeButton(
    text: String,
    highlighted: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
){
    Box (
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (highlighted) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(15.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = text)
        }
    }
}