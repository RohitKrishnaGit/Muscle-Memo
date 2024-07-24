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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.cs346.musclememo.classes.User
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.api.services.PublicWorkout
import com.cs346.musclememo.api.services.WorkoutRequest
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.screens.viewmodels.JoinWorkoutViewModel
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel

@Composable
fun WorkoutItem(index: Int, workout: PublicWorkout, isOwner: Boolean, onClick: (PublicWorkout) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${index + 1}. ${workout.name}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = workout.description)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                if (!isOwner) {
                    Button(
                        onClick = {
                            onClick(workout)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Request to Join", color = MaterialTheme.colorScheme.onPrimary)
                    }
                } else {
                    Button(
                        onClick = {
                            onClick(workout)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("View Requests", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutList(viewModel: JoinWorkoutViewModel) {
    LaunchedEffect(Unit) {
        viewModel.clearWorkouts()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(viewModel.workouts) { index, workout ->
            WorkoutItem(
                index = index,
                workout = workout,
                isOwner = viewModel.publicWorkoutTab == "Owned",
                onClick = {
                    if (viewModel.publicWorkoutTab == "Owned") {
                        viewModel.selectRequests(workout.id)
                    } else {
                        viewModel.sendRequest(workout.id)
                    }
                }
            )
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
                    viewModel.getMyWorkouts()
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

@Composable
fun IncomingWorkoutRequestCard(request: WorkoutRequest, viewModel: JoinWorkoutViewModel, requestIndex: Int) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = request.sender.username, fontSize = 20.sp)
            Text(text = request.sender.experience)
            Text(text = request.sender.gender)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.acceptRequest(request.id)
                        viewModel.removeIncomingWorkoutRequest(requestIndex)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Accept", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = {
                        println("reject workout request")
//                        viewModel.rejectWorkoutRequest(request.id)
//                        viewModel.removeIncomingWorkoutRequest(requestIndex)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reject", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}
