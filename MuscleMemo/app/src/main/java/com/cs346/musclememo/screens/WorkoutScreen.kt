package com.cs346.musclememo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.genericcomponents.Colors
import com.cs346.musclememo.genericcomponents.MMButton

@Composable
fun WorkoutScreenContent(
    //viewModel: WorkoutScreenViewModel = hiltViewModel()
) {
    WorkoutScreen(
        //state = state,
    )
}

data class Exercise(
    var weight: MutableState<Int>,
    var reps: MutableState<Int>
)

// a workout consists a list of exercises.
// each element is a map where the key is the exerciseId and the value is a list of sets of
data class Workout(
    var workoutName: MutableState<String>,
    var exercises: MutableList<MutableMap<Int, MutableList<Exercise>>>
)

// todo: replace with actual data from backend
val getExerciseById = mapOf(
    0 to "Bench",
    1 to "Squat",
    2 to "Deadlift"
)

@Preview(showBackground = true)
@Composable
private fun WorkoutScreen() {
    val scrollState = rememberScrollState()
    var sheetVisible by remember { mutableStateOf(false) }
    val currentWorkout by remember {
        mutableStateOf(
            Workout(
                workoutName = mutableStateOf("New Workout"),
                exercises = mutableListOf(
                    mutableMapOf(
                        0 to mutableListOf(
                            Exercise(mutableStateOf(1), mutableStateOf(2)),
                            Exercise(mutableStateOf(3), mutableStateOf(4)),
                        ),
                    ),
                    mutableMapOf(
                        1 to mutableListOf(
                            Exercise(mutableStateOf(11), mutableStateOf(12)),
                            Exercise(mutableStateOf(13), mutableStateOf(14)),
                        ),
                    ),
                    mutableMapOf(
                        2 to mutableListOf(
                            Exercise(mutableStateOf(21), mutableStateOf(22)),
                            Exercise(mutableStateOf(23), mutableStateOf(24)),
                        ),
                    ),
                    mutableMapOf(
                        0 to mutableListOf(
                            Exercise(mutableStateOf(1), mutableStateOf(2)),
                            Exercise(mutableStateOf(3), mutableStateOf(4)),
                        ),
                    ),
                )
            )
        )
    }

    // main screen
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Workouts",
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            MMButton(
                onClick = {
                    sheetVisible = true
                },
                buttonText = "Start A New Workout",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Templates",
                    fontSize = 20.sp
                )
                IconButton(onClick = {
                    // todo: add a new template
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }

        // current workout sheet that slides up
        WorkoutSheet(
            sheetVisible = sheetVisible,
            currentWorkout = currentWorkout,
            hideSheet = { sheetVisible = false },
            scrollState = scrollState
        )
    }
}

@Composable
fun WorkoutSheet(
    sheetVisible: Boolean,
    currentWorkout: Workout,
    hideSheet: () -> Unit,
    scrollState: ScrollState
) {
    // dialogs
    var showChangeWorkoutNameDialog by remember { mutableStateOf(false) }
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }

    CustomDialog(
        showDialog = showChangeWorkoutNameDialog,
        title = "Change Workout Name",
        initialValue = currentWorkout.workoutName.value,
        onConfirm = { newValue -> currentWorkout.workoutName.value = newValue },
        onDismissRequest = { showChangeWorkoutNameDialog = false }
    )

    CustomDialog(
        showDialog = showDeleteExerciseDialog,
        title = "Delete Exercise",
        text = "Are you sure you want to delete this exercise?",
        initialValue = "",
        onConfirm = { _ ->
            // TODO: Delete the exercise
            showDeleteExerciseDialog = false
        },
        onDismissRequest = { showDeleteExerciseDialog = false },
        hasInputField = false,
    )

    AnimatedVisibility(
        visible = sheetVisible,
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
                        .background(Color.DarkGray)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = currentWorkout.workoutName.value,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        IconButton(onClick = {
                            showChangeWorkoutNameDialog = true
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
                        }
                    }

                    IconButton(onClick = {
                        hideSheet()
                    }) {
                        Icon(
                            Icons.Filled.KeyboardDoubleArrowDown,
                            contentDescription = "Minimize sheet",
                            tint = Color.White
                        )
                    }
                }

                // content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                ) {
                    currentWorkout.exercises.forEachIndexed { index, exerciseMap ->
                        exerciseMap.forEach { (exerciseId, sets) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = getExerciseById[exerciseId] ?: "Unknown Exercise",
                                    color = Colors.PRIMARY.color,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )

                                IconButton(onClick = {
                                    showDeleteExerciseDialog = true
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                                }
                            }


                            Spacer(modifier = Modifier.height(4.dp))

                            val setFields = listOf(
                                Pair("Set", 1f),
                                Pair("Weight (kg)", 4f),
                                Pair("Reps", 4f),
                                Pair("", 1f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                for ((text, weight) in setFields) {
                                    Box(
                                        modifier = Modifier.weight(weight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = text,
                                            color = Colors.SECONDARY.color,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Column {
                                sets.forEachIndexed { index, set ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .weight(setFields[0].second),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                color = Colors.SECONDARY.color,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }

                                        TextField(
                                            value = set.weight.value.toString(),
                                            onValueChange = { newValue ->
                                                set.weight.value = newValue.toIntOrNull() ?: 0
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier
                                                .weight(setFields[1].second)
                                                .padding(start = 8.dp),
                                        )

                                        TextField(
                                            value = set.reps.value.toString(),
                                            onValueChange = { newValue ->
                                                set.reps.value = newValue.toIntOrNull() ?: 0
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier
                                                .weight(setFields[2].second)
                                                .padding(start = 8.dp, end = 8.dp),
                                        )

                                        IconButton(
                                            onClick = {
                                                // todo: delete set
                                            },
                                            modifier = Modifier.weight(setFields[3].second)
                                        ) {
                                            Icon(Icons.Filled.Close, contentDescription = "Delete Set")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            MMButton(
                                onClick = {
                                    // todo: add a new set to the exercise
                                },
                                buttonText = "Add Set",
                                backgroundColor = Colors.PRIMARY.color
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    MMButton(
                        onClick = {
                            // todo: add a new exercise
                        },
                        buttonText = "Add New Exercise",
                        backgroundColor = Colors.SECONDARY.color
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MMButton(
                            onClick = {
                                // todo: cancel workout
                            },
                            buttonText = "Cancel Workout",
                            backgroundColor = Colors.DANGER.color,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        MMButton(
                            onClick = {
                                // todo finish workout
                            },
                            buttonText = "Finish Workout",
                            backgroundColor = Colors.SUCCESS.color,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialog(
    showDialog: Boolean,
    title: String,
    text: String = "",
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismissRequest: () -> Unit,
    hasInputField: Boolean = true,
) {
    if (showDialog) {
        var inputValue by remember { mutableStateOf(initialValue) }

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(title) },
            text = {
                if (hasInputField) {
                    TextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = { Text("Input") }
                    )
                } else {
                    Text(text)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(if (hasInputField) inputValue else "")
                    onDismissRequest()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        )
    }
}