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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.cs346.musclememo.classes.Colors
import com.cs346.musclememo.classes.Workout
import com.cs346.musclememo.genericcomponents.MMButton
import com.cs346.musclememo.genericcomponents.MMDialog

@Composable
fun WorkoutScreenContent(
    //viewModel: WorkoutScreenViewModel = hiltViewModel()
) {
    WorkoutScreen(
        //state = state,
    )
}

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
        mutableStateOf(Workout())
    }
//    val currentWorkout by remember {
//        mutableStateOf(
//            Workout(
//                workoutName = mutableStateOf("New Workout"),
//                exercises = mutableStateListOf(
//                    mutableStateMapOf(
//                        0 to mutableStateListOf(
//                            Exercise(mutableStateOf(null), mutableStateOf(null)),
//                            Exercise(mutableStateOf(null), mutableStateOf(null)),
//                        ),
//                    ),
//                    mutableStateMapOf(
//                        1 to mutableStateListOf(
//                            Exercise(mutableStateOf(null), mutableStateOf(null)),
//                            Exercise(mutableStateOf(null), mutableStateOf(null)),
//                        ),
//                    ),
//                    mutableStateMapOf(
//                        2 to mutableStateListOf(
//                            Exercise(mutableStateOf(null), mutableStateOf(null)),
//                            Exercise(mutableStateOf(null), mutableStateOf(null)),
//                        ),
//                    ),
//                )
//            )
//        )
//    }

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
                    sheetVisible = true
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
    var showCancelWorkoutDialog by remember { mutableStateOf(false) }
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }
    var selectedExerciseIndex by remember { mutableStateOf(-1) }

    MMDialog(
        showDialog = showChangeWorkoutNameDialog,
        title = "Change Workout Name",
        initialValue = currentWorkout.getWorkoutName(),
        onConfirm = { newValue -> currentWorkout.setWorkoutName(newValue) },
        onDismissRequest = { showChangeWorkoutNameDialog = false },
        hasText = false
    )

    MMDialog(
        showDialog = showCancelWorkoutDialog,
        title = "Cancel Workout",
        text = "Are you sure you want to cancel this workout?",
        initialValue = "",
        onConfirm = { _ ->
            currentWorkout.setWorkout()
            hideSheet()
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
                currentWorkout.removeExercise(selectedExerciseIndex)
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
                        .background(Colors.SECONDARY.color)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 16.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
                    ) {
                        Text(
                            text = currentWorkout.getWorkoutName(),
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
                // should be lazy column but it crashes whenever i switch to lazy column so idk
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    currentWorkout.exercises.forEachIndexed { exerciseIndex, exerciseMap ->
                        exerciseMap.forEach { (exerciseId, sets) ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Colors.LIGHTGRAY.color)
                                    .padding(start = 16.dp, end = 8.dp)
                            ) {
                                Text(
                                    text = getExerciseById[exerciseId] ?: "Unknown Exercise",
                                    color = Colors.SECONDARY.color,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )

                                IconButton(onClick = {
                                    showDeleteExerciseDialog = true
                                    selectedExerciseIndex = exerciseIndex
                                }) {
                                    Icon(
                                        Icons.Outlined.Delete,
                                        contentDescription = "Delete Exercise",
                                        tint = Colors.DANGER.color)
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 8.dp)
                            ) {
                                val setFields = listOf(
                                    Pair("Set", 1f),
                                    Pair("Weight (kg)", 4f),
                                    Pair("Reps", 4f),
                                    Pair("", 1f)
                                )

                                if (sets.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
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
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                sets.forEachIndexed { setIndex, set ->
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
                                                text = "${setIndex + 1}",
                                                color = Colors.SECONDARY.color,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }

                                        OutlinedTextField(
                                            value = set.weight.value?.toString() ?: "",
                                            onValueChange = { newValue ->
                                                set.weight.value = newValue.toIntOrNull()
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier
                                                .weight(setFields[1].second)
                                                .padding(start = 8.dp),
                                        )

                                        OutlinedTextField(
                                            value = set.reps.value?.toString() ?: "",
                                            onValueChange = { newValue ->
                                                set.reps.value = newValue.toIntOrNull()
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier
                                                .weight(setFields[2].second)
                                                .padding(start = 8.dp, end = 8.dp),
                                        )

                                        IconButton(
                                            onClick = {
                                                currentWorkout.removeSet(exerciseIndex, setIndex)
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
                                    currentWorkout.addNewSet(exerciseIndex)
                                },
                                text = "Add Set",
                                backgroundColor = Colors.PRIMARY.color,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    MMButton(
                        onClick = {
                            // todo: select a specific exercise
                            val selectedExerciseId = java.util.Random().nextInt(3)
                            currentWorkout.addNewExercise(selectedExerciseId)
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
                                // todo: send data to backend
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