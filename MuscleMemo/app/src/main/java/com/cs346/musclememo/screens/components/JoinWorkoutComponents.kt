package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.api.services.PublicWorkout
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.screens.viewmodels.JoinWorkoutViewModel
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel

@Composable
fun WorkoutItem(index: Int, workout: PublicWorkout, onClick: (PublicWorkout) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(workout) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${index + 1}. ${workout.name}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = workout.description)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun WorkoutList(viewModel: JoinWorkoutViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(viewModel.workouts) { index, workout ->
            WorkoutItem(index = index, workout = workout, onClick = { println(workout) })
        }
    }
}

@Composable
fun PublicWorkoutTabs(
    viewModel: JoinWorkoutViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        TabButton(
            text = "Search",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.publicWorkoutTab == "Search",
            onClick = {
                if (viewModel.publicWorkoutTab != "Search") {
                    viewModel.updateWorkoutTab("Search")
                }
            }
        )
        TabButton(
            text = "Workouts",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.publicWorkoutTab == "Current",
            onClick = {
                if (viewModel.publicWorkoutTab != "Current") {
                    viewModel.updateWorkoutTab("Current")
                }
            }
        )
        TabButton(
            text = "My Workouts",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.publicWorkoutTab == "Owned",
            onClick = {
                if (viewModel.publicWorkoutTab != "Owned") {
                    viewModel.updateWorkoutTab("Owned")
                }
            }
        )
    }
}

@Composable
fun TabButton(
    text: String,
    highlighted: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (highlighted) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectExperienceLevel(
    experience: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    updateExperience: (String) -> Unit
) {
    val experienceLevels: List<String> = listOf(
        "Novice",
        "Intermediate",
        "Professional"
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            TextField(
                value = experience,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.exposedDropdownSize()
            ) {
                experienceLevels.forEach { experience ->
                    DropdownMenuItem(
                        text = { Text(text = experience) },
                        onClick = {
                            updateExperience(experience)
                            onExpandedChange(false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
