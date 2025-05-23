package com.cs346.musclememo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.api.services.PublicWorkout
import com.cs346.musclememo.screens.components.IncomingWorkoutRequestCard
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.components.PublicWorkoutTabs
import com.cs346.musclememo.screens.components.SelectExperienceLevel
import com.cs346.musclememo.screens.components.SelectGenderTag
import com.cs346.musclememo.screens.components.TopAppBar
import com.cs346.musclememo.screens.components.WorkoutList
import com.cs346.musclememo.screens.viewmodels.JoinWorkoutViewModel
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun JoinWorkoutScreen(
    viewModel: JoinWorkoutViewModel = viewModel<JoinWorkoutViewModel>(),
    bottomBarState: MutableState<Boolean>
) {
    LaunchedEffect (Unit) {
        viewModel.setBottomNavBar = {bottomBarState.value = it}
    }
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
                    viewModel.updateCreateWorkoutVisible(true)
                }, text = "Start A Public Workout", maxWidth = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            PublicWorkoutTabs(viewModel)
            Spacer(modifier = Modifier.height(8.dp))
            PublicWorkoutContent(viewModel)
        }
        WorkoutRequests(viewModel)
        WorkoutChat(viewModel, bottomBarState)
    }
    CreatePublicWorkoutSheet(viewModel = viewModel)
    ResultsSheet(viewModel)
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
            }
        }
    } else if (viewModel.publicWorkoutTab == "Current") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchResults(viewModel, viewModel.joinedWorkouts)
            }
        }
    } else if (viewModel.publicWorkoutTab == "Owned") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchResults(viewModel, viewModel.myWorkouts)
            }
        }
    }
}

@Composable
fun SearchResults(
    viewModel: JoinWorkoutViewModel, publicWorkouts: MutableList<PublicWorkout>
) {
    WorkoutList(viewModel, publicWorkouts)
}

@Composable
fun SearchFilters(
    viewModel: JoinWorkoutViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Friends Only")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = viewModel.friendsOnlyFilter,
                onCheckedChange = { viewModel.updateFriendsOnlyFilter(it) })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Gender Tag")
        Spacer(modifier = Modifier.height(8.dp))
        SelectGenderTag(
            gender = viewModel.genderFilter,
            expanded = viewModel.genderFilterExpanded,
            onExpandedChange = viewModel::updateGenderFilterExpanded,
            updateGender = viewModel::updateGenderFilter
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Experience Level")
        Spacer(modifier = Modifier.height(8.dp))
        SelectExperienceLevel(
            experience = viewModel.experienceFilter,
            expanded = viewModel.experienceFilterExpanded,
            onExpandedChange = viewModel::updateExperienceFilterExpanded,
            updateExperience = viewModel::updateExperienceFilter,
        )

        Spacer(modifier = Modifier.height(8.dp))

        MMButton(
            onClick = {
                viewModel.getWorkouts {
                    viewModel.updateResultsScreenVisible(true)
                }
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
    BackHandler(viewModel.createWorkoutVisible) {
        viewModel.updateCreateWorkoutVisible(false)
    }

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
                OutlinedTextField(value = viewModel.createWorkoutName,
                    onValueChange = { viewModel.updateCreateWorkoutName(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    placeholder = { Text("Untitled Workout") })
                Spacer(modifier = Modifier.height(15.dp))
                Text("Description")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = viewModel.createWorkoutDescription,
                    onValueChange = { viewModel.updateCreateWorkoutDescription(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    placeholder = { Text("Write a description") })
                Spacer(modifier = Modifier.height(15.dp))
                Text("Gender Tag")
                Spacer(modifier = Modifier.height(8.dp))
                SelectGenderTag(
                    gender = viewModel.createWorkoutGender,
                    expanded = viewModel.genderExpanded,
                    onExpandedChange = viewModel::updateGenderExpanded,
                    updateGender = viewModel::updateCreateWorkoutGender
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
                            viewModel.createWorkout {
                                viewModel.getJoinedWorkouts()
                                viewModel.getMyWorkouts()
                            }
                            viewModel.clearCreateForm()
                            viewModel.updateCreateWorkoutVisible(false)
                        }
                    },
                    maxWidth = true,
                    text = "Create Public Workout",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ResultsSheet(
    viewModel: JoinWorkoutViewModel
) {
    BackHandler(viewModel.resultsScreenVisible) {
        viewModel.updateResultsScreenVisible(false)
    }

    AnimatedVisibility(
        visible = viewModel.resultsScreenVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(text = "Results") {
                    viewModel.updateResultsScreenVisible(false)
                }
                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)){
                    SearchResults(viewModel, viewModel.workouts)
                }
            }
        }
    }
}


@Composable
fun RequestsHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = onClose) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close", tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            "Requests to Join Workout",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 20.sp
        )
    }
}

@Composable
fun WorkoutRequests(viewModel: JoinWorkoutViewModel) {
    BackHandler (viewModel.requestsVisible)  {
        viewModel.updateRequestsVisible(false)
    }
    AnimatedVisibility(
        visible = viewModel.requestsVisible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            RequestsHeader(onClose = { viewModel.updateRequestsVisible(false) })
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(viewModel.workoutRequests) { index, request ->
                    IncomingWorkoutRequestCard(
                        request = request, viewModel = viewModel, requestIndex = index
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutChat(
    viewModel: JoinWorkoutViewModel,
    bottomBarState: MutableState<Boolean>
) {
    Chat(
        visible = viewModel.workoutChatVisible,
        startChat = {
            DisposableEffect(Unit) {
                // Concatenate user ids to generate room id
                val workout = viewModel.selectedWorkout
                if (workout != null) {
                    val roomId = "workout-${workout.id}"
                    viewModel.connectSocket(roomId)
                }
                onDispose {
                    viewModel.disconnectSocket()
                }
            }
        },
        profilePicture = null,
        friendUsername = viewModel.selectedWorkout?.name ?: "Untitled Workout",
        messages = viewModel.receivedMessages,
        currentMessage = viewModel.currentMessage,
        updateCurrentMessage = viewModel::updateCurrentMessage,
        sendMessage = {
            if (AppPreferences.refreshToken != null && viewModel.currentMessage.isNotBlank()) {
                viewModel.sm.sendMessage(viewModel.currentMessage)
                viewModel.updateCurrentMessage("")
            }
        },
        isTransitioning = {},
        onBackPressed = {
            viewModel.updateWorkoutChatVisible(false)
            bottomBarState.value = true
        },
        reportable = false,
        remove = {
            viewModel.deletePublicWorkout(viewModel.selectedWorkout?.id ?: -1)
            viewModel.updateWorkoutChatVisible(false)
            bottomBarState.value = true
        }
    )
}