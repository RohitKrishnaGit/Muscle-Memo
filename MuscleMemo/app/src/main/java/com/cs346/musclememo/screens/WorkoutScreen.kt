package com.cs346.musclememo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.components.ExerciseSets
import com.cs346.musclememo.screens.components.ExerciseTitle
import com.cs346.musclememo.screens.components.WorkoutHistoryCard
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel
import java.util.Random

@Composable
fun WorkoutScreen(
    viewModel :WorkoutScreenViewModel
) {
    // TODO Add back later for all workouts
    LaunchedEffect(Unit){
        viewModel.getExercises()
    }

    // main screen
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Workouts",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            MMButton(
                onClick = {
                    viewModel.resetWorkout()
                    viewModel.setWorkoutScreenVisible(true)
                },
                text = "Start A New Workout",
                maxWidth = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Templates",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = {
                    // todo: add a new template
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // current workout sheet that slides up
        WorkoutSheet(
            viewModel = viewModel
        )
    }
}

@Composable
fun WorkoutSheet(
    viewModel: WorkoutScreenViewModel
) {
    // dialogs
    var showChangeWorkoutNameDialog by remember { mutableStateOf(false) }
    var showCancelWorkoutDialog by remember { mutableStateOf(false) }
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }
    var selectedExerciseIndex by remember { mutableIntStateOf(-1) }

    MMDialog(
        showDialog = showChangeWorkoutNameDialog,
        title = "Change Workout Name",
        initialValue = viewModel.currentWorkout.name,
        onConfirm = { viewModel.setWorkoutName(it) },
        onDismissRequest = { showChangeWorkoutNameDialog = false },
        hasText = false
    )

    MMDialog(
        showDialog = showCancelWorkoutDialog,
        title = "Cancel Workout",
        text = "Are you sure you want to cancel this workout?",
        initialValue = "",
        onConfirm = {
            viewModel.setWorkoutScreenVisible(false)
        },
        onDismissRequest = { showCancelWorkoutDialog = false },
        hasInputField = false,
    )

    MMDialog(
        showDialog = showDeleteExerciseDialog,
        title = "Delete Exercise",
        text = "Are you sure you want to delete this exercise?",
        initialValue = "",
        onConfirm = { _ ->
            if (selectedExerciseIndex != -1) {
                viewModel.removeExercise(selectedExerciseIndex)
                selectedExerciseIndex = -1
            }
            showDeleteExerciseDialog = false
        },
        onDismissRequest = {
            selectedExerciseIndex = -1
            showDeleteExerciseDialog = false
        },
        hasInputField = false,
    )

    AnimatedVisibility(
        visible = viewModel.workoutVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // top bar
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 24.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                    ) {
                        Text(
                            text = viewModel.currentWorkout.name,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 20.sp
                        )
                        IconButton(onClick = {
                            showChangeWorkoutNameDialog = true
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }

                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(items = viewModel.currentWorkout.exercises) { index, exerciseIt ->
                        Column {
                            ExerciseTitle(
                                exerciseRef = exerciseIt.exerciseRef,
                                onClick = {
                                    showDeleteExerciseDialog = true
                                    selectedExerciseIndex = index
                                }
                            )
                            ExerciseSets(
                                sets = exerciseIt.exerciseSet,
                                deleteSet = { setIndex ->
                                    viewModel.removeSet(index, setIndex)
                                },
                                addSet = {
                                    viewModel.addSet(index)
                                },
                                editSet = { weight, reps, setIndex ->
                                    viewModel.editSet(weight, reps, index, setIndex)
                                }
                            )
                        }

                    }
                    item {
                        Column {
                            MMButton(
                                onClick = {
                                    // todo: select a specific exercise

                                    val selectedExerciseId = Random().nextInt(viewModel.exerciseRefs.size)
                                    viewModel.addNewExercise(exerciseRef = viewModel.exerciseRefs[selectedExerciseId])
                                },
                                text = "Add New Exercise",
                                maxWidth = true,
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            ) {
                                MMButton(
                                    onClick = {
                                        showCancelWorkoutDialog = true
                                    },
                                    text = "Cancel Workout",
                                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                                    textColor = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                MMButton(
                                    onClick = {
                                        viewModel.finishWorkout()
                                        viewModel.setWorkoutScreenVisible(false)
                                        viewModel.setSummaryScreenVisible(true)
                                    },
                                    text = "Finish Workout",
                                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    AnimatedVisibility(
        visible = viewModel.summaryVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Keep up the good work!",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.weight(1f))
                WorkoutHistoryCard(workout = viewModel.currentWorkout, onClick = {})
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    MMButton(onClick = { viewModel.setSummaryScreenVisible(false) }, text = "Done")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
