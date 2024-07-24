package com.cs346.musclememo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.components.PublicWorkoutTabs
import com.cs346.musclememo.screens.components.SelectExperienceLevel
import com.cs346.musclememo.screens.components.TopAppBar
import com.cs346.musclememo.screens.components.WorkoutList
import com.cs346.musclememo.screens.viewmodels.JoinWorkoutViewModel

@Composable
fun JoinWorkoutScreen(
    viewModel: JoinWorkoutViewModel = viewModel<JoinWorkoutViewModel>()
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Join a Workout",
                fontSize = 40.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MMButton(
                onClick = {
//                    viewModel.resetWorkout()
                    viewModel.updateCreateWorkoutVisible(true)
                },
                text = "Start A Public Workout",
                maxWidth = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            PublicWorkoutTabs(viewModel)
            Spacer(modifier = Modifier.height(8.dp))
            PublicWorkoutContent(viewModel)
        }
    }
    CreatePublicWorkoutSheet(viewModel = viewModel)
}

@Composable
fun PublicWorkoutContent(
    viewModel: JoinWorkoutViewModel
) {
    if (viewModel.publicWorkoutTab == "Search") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SearchFilters(viewModel)
                SearchResults(viewModel)
            }
        }
    }
}

@Composable
fun SearchResults(
    viewModel: JoinWorkoutViewModel
) {
    WorkoutList(viewModel)
}

@Composable
fun SearchFilters(
    viewModel: JoinWorkoutViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Friends Only")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = viewModel.friendsOnlyFilter,
                onCheckedChange = { viewModel.updateFriendsOnlyFilter(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.genderFilter.orEmpty(),
            onValueChange = { viewModel.updateGenderFilter(it.takeIf { it.isNotEmpty() }) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Gender") },
            placeholder = { Text("Enter gender") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.experienceFilter.orEmpty(),
            onValueChange = { viewModel.updateExperienceFilter(it.takeIf { it.isNotEmpty() }) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Experience") },
            placeholder = { Text("Novice") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        MMButton(
            onClick = {
                viewModel.getWorkouts()
            },
            maxWidth = true,
            text = "Search",
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
fun CreatePublicWorkoutSheet(
    viewModel: JoinWorkoutViewModel
) {
    AnimatedVisibility(
        visible = viewModel.createWorkoutVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        NewPublicWorkout(viewModel)
    }
}

@Composable
fun NewPublicWorkout(viewModel: JoinWorkoutViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(text = "New Public Workout") {
                viewModel.updateCreateWorkoutVisible(false)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text("Workout Name")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = viewModel.createWorkoutName,
                    onValueChange = { viewModel.updateCreateWorkoutName(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    placeholder = { Text("Power Gym Meetup 6/28") }
                )
                // Date
                Spacer(modifier = Modifier.height(15.dp))
                Text("Experience Level")
                Spacer(modifier = Modifier.height(8.dp))
                SelectExperienceLevel(
                    experience = viewModel.createWorkoutExperience,
                    expanded = viewModel.experienceExpanded,
                    onExpandedChange = viewModel::updateExperienceExpanded,
                    updateExperience = viewModel::updateCreateWorkoutExperience
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Description")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = viewModel.createWorkoutDescription,
                    onValueChange = { viewModel.updateCreateWorkoutDescription(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    placeholder = { Text("Going for PRs today") }
                )
                Spacer(modifier = Modifier.height(15.dp))
                if (viewModel.showCreateError) {
                    Text(
                        text = "Please fill in the form",
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                MMButton(
                    onClick = {
                        if (viewModel.validateForm()) {
                            viewModel.createWorkout()
                            viewModel.clearCreateForm()
                            viewModel.updateCreateWorkoutVisible(false)
                        }
                    },
                    maxWidth = true,
                    text = "Finish Workout",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
