package com.cs346.musclememo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.components.ExerciseSets
import com.cs346.musclememo.screens.components.ExerciseTitle
import com.cs346.musclememo.screens.components.TopAppBar
import com.cs346.musclememo.screens.components.WorkoutHistoryCard
import com.cs346.musclememo.screens.components.getTransitionDirection
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel

@Composable
fun WorkoutScreen(
    viewModel :WorkoutScreenViewModel
) {
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
                    viewModel.fetchCombinedExercises()
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

// TODO: switch with WorkoutScreen when changes are ready
@Composable
fun NewWorkoutScreen (
    viewModel :WorkoutScreenViewModel
){
    BackHandler (viewModel.chooseWorkoutVisible) {
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

            Spacer(modifier = Modifier.height(16.dp))

            MMButton(
                onClick = {
                    viewModel.resetWorkout()
                    viewModel.setWorkoutScreenVisible(true)
                },
                text = "Start A New Workout",
                maxWidth = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "History", fontSize = 24.sp)
            // DisplayHistory(viewModel)
        }
    }
    newWorkoutSheet(
        viewModel = viewModel
    )
//    WorkoutHistorySheet(
//        workout = viewModel.currentWorkout,
//        visible = viewModel.showCurrentWorkout,
//        onBackPressed = viewModel::onBackPressed
//    )
}

@Composable
fun newWorkoutSheet(
    viewModel: WorkoutScreenViewModel
){
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
            when (targetState.screen){
                WorkoutScreenViewModel.WorkoutState.NEW_WORKOUT -> {
                    NewWorkout(viewModel = viewModel)
                }
                WorkoutScreenViewModel.WorkoutState.CURRENT_WORKOUT -> {
                    CurrentWorkout(viewModel = viewModel)
                }
            }

        }
    }
}

@Composable
fun NewWorkout(viewModel: WorkoutScreenViewModel){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ){
        Column (
            Modifier.fillMaxSize()
        ){
            TopAppBar(text = "New Workout") {
                viewModel.onBackPressed()
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentWorkout(
    viewModel: WorkoutScreenViewModel
){
    // TODO: add WorkoutSheet contents in here
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WorkoutSheet(
    viewModel: WorkoutScreenViewModel
) {
    var selectedExerciseIndex by remember { mutableIntStateOf(-1) }
    var selectedExercise by remember { mutableStateOf<ExerciseRef?>(null) }

    MMDialog(
        showDialog = viewModel.showChangeWorkoutNameDialog,
        title = "Change Workout Name",
        onConfirm = {
            viewModel.setWorkoutName(viewModel.tempWorkoutName)
        },
        onDismissRequest = { viewModel.updateShowChangeWorkoutNameDialog(false) },
        body = {
            TextField(
                value = viewModel.tempWorkoutName,
                onValueChange = { viewModel.tempWorkoutName = it },
                label = { Text("Workout Name") }
            )
        },
        confirmButtonText = "Save",
        cancelButtonText = "Cancel"
    )

    MMDialog(
        showDialog = viewModel.showCancelWorkoutDialog,
        title = "Cancel Workout",
        onConfirm = {
            viewModel.setWorkoutScreenVisible(false)
            viewModel.updateShowCancelWorkoutDialog(false)
        },
        onDismissRequest = { viewModel.updateShowCancelWorkoutDialog(false) },
        body = {
            Text("Are you sure you want to cancel this workout?")
        }
    )

    MMDialog(
        showDialog = viewModel.showDeleteExerciseDialog,
        title = "Delete Exercise",
        onConfirm = {
            if (selectedExerciseIndex != -1) {
                viewModel.removeExercise(selectedExerciseIndex)
                selectedExerciseIndex = -1
            }
            viewModel.updateShowRemoveExerciseDialog(false)
        },
        onDismissRequest = {
            selectedExerciseIndex = -1
            viewModel.updateShowRemoveExerciseDialog(false)
        },
        body = {
            Text("Are you sure you want to delete this exercise?")
        }
    )

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
                    onValueChange = { viewModel.updateNewExerciseRef(viewModel.newExerciseRef.copy(name = it)) },
                    label = { Text("Exercise Name") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.newExerciseRef.durationVSReps,
                        onClick = { viewModel.updateNewExerciseRef(viewModel.newExerciseRef.copy(durationVSReps = true)) }
                    )
                    Text("Repetitions", modifier = Modifier.align(Alignment.CenterVertically))
                    RadioButton(
                        selected = !viewModel.newExerciseRef.durationVSReps,
                        onClick = { viewModel.updateNewExerciseRef(viewModel.newExerciseRef.copy(durationVSReps = false)) }
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

    MMDialog(
        showDialog = viewModel.showEditCustomExerciseDialog,
        title = "Edit Exercise",
        onConfirm = {
            selectedExercise?.let { exercise ->
                viewModel.updateDialogButtonsEnabled(false)
                viewModel.updateCustomExercise(exercise)
            }
        },
        onDismissRequest = {
            selectedExercise = null
            viewModel.updateShowEditCustomExerciseDialog(false)
            viewModel.updateDialogErrorMessage("")
            viewModel.updateDialogButtonsEnabled(true)
        },
        body = {
            Column {
                selectedExercise?.let { exercise ->
                    TextField(
                        value = exercise.name,
                        onValueChange = { newName ->
                            selectedExercise = exercise.copy(name = newName)
                        },
                        label = { Text("Exercise Name") }
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = exercise.durationVSReps,
                            onClick = {
                                selectedExercise = exercise.copy(durationVSReps = true)
                            }
                        )
                        Text("Repetitions", modifier = Modifier.align(Alignment.CenterVertically))
                        RadioButton(
                            selected = !exercise.durationVSReps,
                            onClick = {
                                selectedExercise = exercise.copy(durationVSReps = false)
                            }
                        )
                        Text("Duration", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = exercise.weight,
                            onCheckedChange = { checked ->
                                selectedExercise = exercise.copy(weight = checked)
                            }
                        )
                        Text("Weight-based", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = exercise.distance,
                            onCheckedChange = { checked ->
                                selectedExercise = exercise.copy(distance = checked)
                            }
                        )
                        Text("Distance-based", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
        },
        errorText = viewModel.dialogErrorMessage,
        confirmButtonText = "Save",
        cancelButtonText = "Cancel"
    )

    MMDialog(
        showDialog = viewModel.showDeleteCustomExerciseDialog,
        title = "Delete Exercise",
        onConfirm = {
            selectedExercise?.let { exercise ->
                viewModel.deleteCustomExercise(exercise)
            }
        },
        onDismissRequest = {
            selectedExercise = null
            viewModel.updateDialogErrorMessage("")
            viewModel.updateShowDeleteCustomExerciseDialog(false)
        },
        body = {
            Column {
                selectedExercise?.let { exercise ->
                    Text("Are you sure you want to delete ${exercise.name}?")
                }
            }
        },
        errorText = viewModel.dialogErrorMessage,
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
                            viewModel.tempWorkoutName = viewModel.currentWorkout.name
                            viewModel.updateShowChangeWorkoutNameDialog(true)
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
                                    viewModel.updateShowRemoveExerciseDialog(true)
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
                                    viewModel.setAddExerciseScreenVisible(true)
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        TextField(
                            value = viewModel.exerciseSearchText,
                            onValueChange = viewModel::updateExerciseSearchText,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 0.dp),
                            placeholder = { Text(" Search Exercises") },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                containerColor = Color.Transparent,
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
                            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                        }
                        Text("Add Exercise", modifier = Modifier.padding(end = 8.dp), color = Color.White)
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { viewModel.updateExerciseSearchMode(true) }) {
                                Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
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
                                viewModel.resetNewExerciseRef()
                                viewModel.updateShowAddNewCustomExerciseDialog(true)
                            }) {
                                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                            }
                        }
                    }
                }
                LazyColumn {
                    val groupedExercises = viewModel.filteredExercises.groupBy { it.name.first().uppercase() }

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
                                        viewModel.addNewExercise(exerciseRef)
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
                                                selectedExercise = exerciseRef
                                                viewModel.updateShowEditCustomExerciseDialog(true)
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Delete") },
                                            onClick = {
                                                showMenu = false
                                                selectedExercise = exerciseRef
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
