package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.Workout
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel

@Composable
fun DisplayHistory(
    viewModel: WorkoutScreenViewModel
){
    val listState = rememberLazyListState()
    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = viewModel.workouts.value){ workout ->
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
                .fillMaxWidth(0.95f)
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
                // TODO: enter time and date, only display day
                DisplayDateDuration("7/20/2024", "23:04")
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
    workout: Workout?,
    visible: Boolean,
    onBackPressed: () -> Unit
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
                TopAppBar(text = workout?.name ?: "Untitled") {
                    onBackPressed()
                }
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ){
                    // TODO: Add date and duration
                    DisplayDateDuration("Saturday, July 20th", "23:04")
                    Spacer(modifier = Modifier.height(20.dp))
                    workout?.exercises?.forEach { exercise ->
                        Column {
                            Text(text = exercise.exerciseRef.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            exercise.exerciseSet.forEachIndexed { index, set ->
                                Text(text = (index+1).toString() + " - " + set.reps.toString() + " x " + set.weight.toString() + " kg")
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayDateDuration(
    date: String,
    time: String
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(Icons.Default.CalendarMonth, null)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = date)
        Spacer(modifier = Modifier.width(20.dp))
        Icon(Icons.Default.Timer, null)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = time)
    }
}