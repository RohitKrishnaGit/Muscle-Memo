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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@Composable
fun WorkoutHistoryCard (
    workout: Workout,
    onClick: () -> Unit
){
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clickable(onClick = onClick)
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
                workout.exercises.forEach { exercise ->
                    Column {
                        Text(text = exercise.exerciseRef.name, fontWeight = FontWeight.Bold)
                        exercise.exerciseSet.forEach { set ->
                            Text(text = set.reps.toString() + " x " + set.weight.toString() + " kg")
                        }
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
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to previous screen")
                    }
                    Text(text = workout?.name ?: "Untitled")
                    Spacer(modifier = Modifier.size(48.dp))
                }
                workout?.exercises?.forEach { exercise ->
                    Column {
                        Text(text = exercise.exerciseRef.name, fontWeight = FontWeight.Bold)
                        exercise.exerciseSet.forEach { set ->
                            Text(text = set.reps.toString() + " x " + set.weight.toString() + " kg")
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }

}