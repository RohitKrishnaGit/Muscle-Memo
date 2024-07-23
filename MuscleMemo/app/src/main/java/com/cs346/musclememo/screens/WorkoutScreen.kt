package com.cs346.musclememo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.screens.components.DisplayHistory
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.components.ExerciseSets
import com.cs346.musclememo.screens.components.ExerciseTitle
import com.cs346.musclememo.screens.components.TopAppBar
import com.cs346.musclememo.screens.components.WorkoutHistoryCard
import com.cs346.musclememo.screens.components.WorkoutHistorySheet
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel
import com.cs346.musclememo.utils.getTransitionDirection
import com.cs346.musclememo.utils.toHourMinuteSeconds
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    viewModel: WorkoutScreenViewModel
) {
    BackHandler(viewModel.workoutVisible) {
        viewModel.onBackPressed()
    }

    // main screen
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Workout",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            MMButton(
                onClick = {
                    viewModel.resetWorkout()
                    viewModel.setWorkoutScreenVisible(true)
                },
                text = "Start A New Workout",
                maxWidth = true
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "History", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(20.dp))
            DisplayHistory(viewModel)
        }
    }
    WorkoutSheet(
        viewModel = viewModel
    )

    WorkoutHistorySheet(
        viewModel = viewModel,
        workout = viewModel.currentHistoryWorkout,
        visible = viewModel.showCurrentWorkout
    )
}

@Composable
fun WorkoutSheet(
    viewModel: WorkoutScreenViewModel
) {
    AnimatedVisibility(
        visible = viewModel.workoutVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        AnimatedContent(
            targetState = viewModel.workoutScreenData,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(300)

                val direction = getTransitionDirection(
                    initialIndex = initialState.screenIndex,
                    targetIndex = targetState.screenIndex,
                )

                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) togetherWith slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            },
            label = "surveyScreenDataAnimation"
        ) { targetState ->
            when (targetState.screen) {
                WorkoutScreenViewModel.WorkoutState.NEW_WORKOUT -> {
                    NewWorkout(viewModel = viewModel)
                }

                WorkoutScreenViewModel.WorkoutState.CURRENT_WORKOUT -> {
                    CurrentWorkout(viewModel = viewModel)
                }

                WorkoutScreenViewModel.WorkoutState.SUMMARY -> {
                    Summary(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NewWorkout(viewModel: WorkoutScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(text = "New Workout") {
                viewModel.onBackPressed()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                MMButton(
                    onClick = {
                        viewModel.resetWorkout()
                        viewModel.updateScreenState(true)
                    },
                    text = "Start An Empty Workout",
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
                        // todo: show template sheet
                    }) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DisplayTemplates(viewModel = viewModel)
                }
            }
        }
    }
    CreateTemplateSheet(viewModel = viewModel)
}

@Composable
fun CurrentWorkout(
    viewModel: WorkoutScreenViewModel
) {
    val listState = rememberLazyListState()
    val localFocusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        while(true) {
            delay(1.seconds)
            viewModel.seconds++
        }
    }

    MMDialog(
        showDialog = viewModel.showChangeWorkoutNameDialog,
        title = "Change Workout Name",
        onConfirm = {
            viewModel.setWorkoutName()
        },
        onDismissRequest = { viewModel.updateShowChangeWorkoutNameDialog(false) },
        body = {
            TextField(
                value = viewModel.tempWorkoutName,
                onValueChange = viewModel::updateTempWorkoutName,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = { Text("Workout Name") }
            )
            Text(viewModel.getDialogBodyText())
        },
        confirmButtonText = "Save",
        cancelButtonText = "Cancel",
        errorText = viewModel.dialogErrorMessage
    )

    MMDialog(
        showDialog = viewModel.showErrorDialog(),
        title = viewModel.getDialogText(),
        onConfirm = viewModel::onDialogConfirm,
        onDismissRequest = viewModel::onDialogDismiss,
        body = {
            Text(viewModel.getDialogBodyText())
        },
        errorText = viewModel.dialogErrorMessage
    )
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
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 24.dp)
            ) {
                Text(text = viewModel.currentWorkout.name, fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary)
                IconButton(onClick = {
                    localFocusManager.clearFocus()
                    viewModel.updateShowChangeWorkoutNameDialog(true)
                }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.Timer, null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = toHourMinuteSeconds(viewModel.seconds), color = MaterialTheme.colorScheme.onPrimary)
            }

            // Display current Exercises
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
                                viewModel.updateShowRemoveExerciseDialog(true)
                                viewModel.selectedExerciseIndex = index
                            }
                        )
                        ExerciseSets(
                            exerciseRef = exerciseIt.exerciseRef,
                            sets = exerciseIt.exerciseSet,
                            deleteSet = { setIndex ->
                                viewModel.removeWorkoutSet(index, setIndex)
                            },
                            addSet = {
                                viewModel.addWorkoutSet(index)
                            }
                        )
                    }

                }
                item {
                    Column {
                        MMButton(
                            onClick = {
                                localFocusManager.clearFocus()
                                viewModel.setAddExerciseScreenVisible(true)
                            },
                            text = "Add New Exercise",
                            maxWidth = true,
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
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
                                    localFocusManager.clearFocus()
                                    viewModel.updateShowCancelWorkoutDialog(true)
                                },
                                text = "Cancel Workout",
                                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                                textColor = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            MMButton(
                                onClick = {
                                    localFocusManager.clearFocus()
                                    viewModel.finishWorkout()
                                    viewModel.updateScreenState(true)
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

    AddExercise(viewModel = viewModel)
}

@Composable
private fun Summary(
    viewModel: WorkoutScreenViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(50.dp))
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
            Spacer(modifier = Modifier.height(50.dp))
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {
                    WorkoutHistoryCard(
                        workout = viewModel.currentWorkout,
                        enabled = false,
                        onClick = {})
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                MMButton(onClick = {
                    viewModel.updateScreenState(true)
                    viewModel.getWorkoutsByUserId()
                }, text = "Done")
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun AddNewCustomExercise(
    viewModel: WorkoutScreenViewModel
) {
    MMDialog(
        showDialog = viewModel.showAddNewCustomExerciseDialog,
        title = "Add New Exercise",
        onConfirm = {
            viewModel.updateDialogButtonsEnabled(false)
            viewModel.createCustomExercise(viewModel.newExerciseRef)
        },
        onDismissRequest = {
            viewModel.updateShowAddNewCustomExerciseDialog(false)
            viewModel.updateDialogErrorMessage("")
            viewModel.updateDialogButtonsEnabled(true)
        },
        body = {
            Column {
                TextField(
                    value = viewModel.newExerciseRef.name,
                    onValueChange = {
                        viewModel.updateNewExerciseRef(
                            viewModel.newExerciseRef.copy(
                                name = it
                            )
                        )
                    },
                    label = { Text("Exercise Name") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.newExerciseRef.durationVSReps,
                        onClick = {
                            viewModel.updateNewExerciseRef(
                                viewModel.newExerciseRef.copy(
                                    durationVSReps = true
                                )
                            )
                        }
                    )
                    Text("Repetitions", modifier = Modifier.align(Alignment.CenterVertically))
                    RadioButton(
                        selected = !viewModel.newExerciseRef.durationVSReps,
                        onClick = {
                            viewModel.updateNewExerciseRef(
                                viewModel.newExerciseRef.copy(
                                    durationVSReps = false
                                )
                            )
                        }
                    )
                    Text("Duration", modifier = Modifier.align(Alignment.CenterVertically))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.newExerciseRef.weight,
                        onCheckedChange = { checked ->
                            viewModel.updateNewExerciseRef(viewModel.newExerciseRef.copy(weight = checked))
                        }
                    )
                    Text("Weight-based", modifier = Modifier.align(Alignment.CenterVertically))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.newExerciseRef.distance,
                        onCheckedChange = { checked ->
                            viewModel.updateNewExerciseRef(viewModel.newExerciseRef.copy(distance = checked))
                        }
                    )
                    Text("Distance-based", modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
        },
        errorText = viewModel.dialogErrorMessage,
        confirmButtonText = "Add",
        cancelButtonText = "Cancel"
    )
}

@Composable
fun EditCustomExercise(
    viewModel: WorkoutScreenViewModel
) {
    MMDialog(
        showDialog = viewModel.showEditCustomExerciseDialog,
        title = "Edit Exercise",
        onConfirm = {
            viewModel.selectedExercise?.let { exercise ->
                viewModel.updateDialogButtonsEnabled(false)
                viewModel.updateCustomExercise(exercise)
            }
        },
        onDismissRequest = {
            viewModel.selectedExercise = null
            viewModel.updateShowEditCustomExerciseDialog(false)
            viewModel.updateDialogErrorMessage("")
            viewModel.updateDialogButtonsEnabled(true)
        },
        body = {
            Column {
                viewModel.selectedExercise?.let { exercise ->
                    TextField(
                        value = exercise.name,
                        onValueChange = { newName ->
                            viewModel.selectedExercise = exercise.copy(name = newName)
                        },
                        label = { Text("Exercise Name") }
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = exercise.durationVSReps,
                            onClick = {
                                viewModel.selectedExercise = exercise.copy(durationVSReps = true)
                            }
                        )
                        Text("Repetitions", modifier = Modifier.align(Alignment.CenterVertically))
                        RadioButton(
                            selected = !exercise.durationVSReps,
                            onClick = {
                                viewModel.selectedExercise = exercise.copy(durationVSReps = false)
                            }
                        )
                        Text("Duration", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = exercise.weight,
                            onCheckedChange = { checked ->
                                viewModel.selectedExercise = exercise.copy(weight = checked)
                            }
                        )
                        Text("Weight-based", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = exercise.distance,
                            onCheckedChange = { checked ->
                                viewModel.selectedExercise = exercise.copy(distance = checked)
                            }
                        )
                        Text(
                            "Distance-based",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        },
        errorText = viewModel.dialogErrorMessage,
        confirmButtonText = "Save",
        cancelButtonText = "Cancel"
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddExercise(
    viewModel: WorkoutScreenViewModel
) {
    AddNewCustomExercise(viewModel)
    EditCustomExercise(viewModel)
    AnimatedVisibility(
        visible = viewModel.addExerciseVisible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                if (viewModel.isExerciseSearchMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                            .background(MaterialTheme.colorScheme.secondary)
                            .heightIn(min = 60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            viewModel.updateExerciseSearchText("")
                            viewModel.updateExerciseSearchMode(false)
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        TextField(
                            value = viewModel.exerciseSearchText,
                            onValueChange = viewModel::updateExerciseSearchText,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 0.dp),
                            placeholder = { Text(" Search Exercises") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedPlaceholderColor = Color.White,
                                unfocusedPlaceholderColor = Color.White
                            )
                        )
                        IconButton(onClick = { viewModel.toggleSort() }) {
                            if (viewModel.isSortedAlphabetically) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Sort,
                                    tint = Color.White,
                                    contentDescription = "Sort Ascending",
                                )
                            } else {
                                Icon(
                                    Icons.AutoMirrored.Filled.Sort,
                                    tint = Color.White,
                                    contentDescription = "Sort Descending",
                                    modifier = Modifier.rotate(180f),
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                            .background(MaterialTheme.colorScheme.secondary)
                            .heightIn(min = 60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { viewModel.setAddExerciseScreenVisible(false) }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                        Text(
                            "Add Exercise",
                            modifier = Modifier.padding(end = 8.dp),
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { viewModel.updateExerciseSearchMode(true) }) {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = { viewModel.toggleSort() }) {
                                if (viewModel.isSortedAlphabetically) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Sort,
                                        tint = Color.White,
                                        contentDescription = "Sort Ascending",
                                    )
                                } else {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Sort,
                                        tint = Color.White,
                                        contentDescription = "Sort Descending",
                                        modifier = Modifier.rotate(180f),
                                    )
                                }
                            }
                            IconButton(onClick = {
                                viewModel.exerciseSearchText = ""
                                viewModel.resetNewExerciseRef()
                                viewModel.updateShowAddNewCustomExerciseDialog(true)
                            }) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                LazyColumn {
                    val groupedExercises =
                        viewModel.filteredExercises.groupBy { it.name.first().uppercase() }

                    groupedExercises.forEach { (initial, exercises) ->
                        stickyHeader {
                            Text(
                                text = initial,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        items(exercises) { exerciseRef ->
                            var showMenu by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clickable(onClick = {
                                        viewModel.exerciseSearchText = ""
                                        viewModel.addWorkoutExercise(exerciseRef)
                                        viewModel.setAddExerciseScreenVisible(false)
                                    }),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = exerciseRef.name,
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .weight(1f),
                                    fontSize = 18.sp
                                )

                                if (exerciseRef.isCustom) {
                                    IconButton(onClick = { showMenu = true }) {
                                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                                    }

                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false },
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Edit") },
                                            onClick = {
                                                showMenu = false
                                                viewModel.selectedExercise = exerciseRef
                                                viewModel.updateShowEditCustomExerciseDialog(true)
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Delete") },
                                            onClick = {
                                                showMenu = false
                                                viewModel.selectedExercise = exerciseRef
                                                viewModel.updateShowDeleteCustomExerciseDialog(true)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayTemplates(
    viewModel: WorkoutScreenViewModel
){

}

@Composable
fun CreateTemplateSheet(
    viewModel: WorkoutScreenViewModel
){

}