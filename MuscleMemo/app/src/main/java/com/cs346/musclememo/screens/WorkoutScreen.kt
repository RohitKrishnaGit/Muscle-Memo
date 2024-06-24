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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.classes.Colors
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.components.ExerciseSets
import com.cs346.musclememo.screens.components.ExerciseTitle
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel
import java.util.Random

@Preview(showBackground = true)
@Composable
fun WorkoutScreen() {
    val viewModel = viewModel<WorkoutScreenViewModel>()

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
                color = Colors.SECONDARY.color
            )

            Spacer(modifier = Modifier.height(16.dp))

            MMButton(
                onClick = {
                    viewModel.resetWorkout()
                    viewModel.setVisible(true)
                },
                text = "Start A New Workout",
                maxWidth = true,
                backgroundColor = Colors.PRIMARY.color
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Templates",
                    fontSize = 20.sp,
                    color = Colors.SECONDARY.color
                )
                IconButton(onClick = {
                    // todo: add a new template
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Colors.PRIMARY.color)
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
            viewModel.setVisible(false)
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
        visible = viewModel.sheetVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
                        .background(Colors.SECONDARY.color)
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
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        IconButton(onClick = {
                            showChangeWorkoutNameDialog = true
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
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
                                exercise = exerciseIt.exercise,
                                onClick = {
                                    showDeleteExerciseDialog = true
                                    selectedExerciseIndex = index
                                }
                            )
                            ExerciseSets(
                                sets = exerciseIt.sets,
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
                                    // todo: custom exercise
                                    val selectedExerciseId = Random().nextInt(2)
                                    viewModel.addNewExercise(exercise = viewModel.exercises[selectedExerciseId])
                                },
                                text = "Add New Exercise",
                                backgroundColor = Colors.PRIMARY.color,
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
                                    backgroundColor = Colors.DANGER.color,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                MMButton(
                                    onClick = {
                                        viewModel.finishWorkout()
                                        viewModel.setVisible(false)
                                    },
                                    text = "Finish Workout",
                                    backgroundColor = Colors.SUCCESS.color,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}