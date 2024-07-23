package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.Workout
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.utils.calculateScore
import com.cs346.musclememo.utils.displayScore
import com.cs346.musclememo.utils.displayTime
import com.cs346.musclememo.utils.epochToDate
import com.cs346.musclememo.utils.getDistance
import com.cs346.musclememo.utils.getWeight
import com.cs346.musclememo.utils.toHourMinuteSeconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
@ExperimentalMaterial3Api
fun DisplayHistory(
    viewModel: WorkoutScreenViewModel
){
    val listState = rememberLazyListState()
    val state = rememberPullToRefreshState()


    PullToRefreshBox (
        modifier = Modifier.fillMaxSize(),
        state = state,
        isRefreshing = viewModel.isHistoryRefreshing,
        onRefresh = viewModel::refreshHistory,
        ){
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val groupedWorkouts = viewModel.groupedWorkouts

            if (groupedWorkouts.isNotEmpty()){
                groupedWorkouts.forEach{(date, workoutsForMonth) ->
                    stickyHeader {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = date)
                        }
                    }
                    items (items = workoutsForMonth) { workout ->
                        WorkoutHistoryCard(
                            workout = workout,
                            onClick = {
                                viewModel.updateCurrentHistoryWorkout(workout)
                                viewModel.updateShowCurrentHistoryWorkout(true)
                            }
                        )
                    }
                }
            }
            else {
                item {
                    Spacer(modifier = Modifier.fillMaxSize())
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(text = "Start your first workout!")
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard (
    workout: Workout,
    enabled: Boolean = true,
    onClick: () -> Unit
){
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled, onClick = onClick)
        ) {
            Column (
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp,
                    )
            ) {
                Text(text = workout.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(5.dp))
                DisplayDateDuration(workout.date, workout.duration, false)
                Spacer(modifier = Modifier.height(5.dp))
                workout.exercises.forEach { exercise ->
                    Column {
                        Text(text = exercise.exerciseSet.size.toString() + " x " + exercise.exerciseRef.name)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun WorkoutHistorySheet(
    viewModel: WorkoutScreenViewModel,
    workout: Workout?,
    visible: Boolean
){
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(onClick = viewModel::onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                    Text(text = workout?.name ?: "Untitled")
                    IconButton(onClick = {
                        if (workout != null) {
                            println(workout.id)
                            viewModel.deleteWorkout(id = workout.id,
                                onSuccess = {
                                    viewModel.onBackPressed()
                                }
                            )
                        } else
                            println("workout is null")
                    }) {
                        Icon(Icons.Default.Delete, "")
                    }
                }

                if (workout != null){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                    ) {
                        DisplayDateDuration(workout.date, workout.duration, true)
                        Spacer(modifier = Modifier.height(10.dp))
                        workout.exercises.forEach { exercise ->
                            Column {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = exercise.exerciseRef.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Row (modifier = Modifier.fillMaxWidth()) {
                                    Text(text = "Sets", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = "Score", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                exercise.exerciseSet.forEachIndexed { index, set ->
                                    Row (modifier = Modifier.fillMaxWidth()){
                                        Text(
                                            text = (index + 1).toString() + " - " + getSetDisplay(
                                                exercise.exerciseRef,
                                                set
                                            ), fontSize = 18.sp
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(text = displayScore(exercise.exerciseRef, calculateScore(set)))
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getSetDisplay(
    exerciseRef: ExerciseRef,
    set: ExerciseSet
): String {
    var setDisplay = ""
    setDisplay += if (exerciseRef.durationVSReps)
        (set.duration?.let { displayTime(it) } ?: "N/A")
    else
        (set.reps?.toString() ?: "N/A") + if (!exerciseRef.weight && !exerciseRef.distance) " reps" else ""
    if (exerciseRef.weight)
        setDisplay += " x " + (getWeight(set.weight)?.toString() ?: "N/A") + " " + AppPreferences.systemOfMeasurementWeight
    if (exerciseRef.distance)
        setDisplay += " x " + (getDistance(set.distance)?.toString() ?: "N/A") + " " + AppPreferences.systemOfMeasurementDistance
    return setDisplay
}

@Composable
fun DisplayDateDuration(
    date: Long,
    duration: Int,
    time: Boolean
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(Icons.Default.CalendarMonth, null)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = epochToDate(date, time), fontSize = 12.sp)
        Spacer(modifier = Modifier.width(20.dp))
        Icon(Icons.Default.Timer, null)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = toHourMinuteSeconds(duration), fontSize = 12.sp)
    }
}