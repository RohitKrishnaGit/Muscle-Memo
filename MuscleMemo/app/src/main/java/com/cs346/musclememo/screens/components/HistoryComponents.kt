package com.cs346.musclememo.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.Workout

@Composable
fun WorkoutHistoryCard (
    workout: Workout
){
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
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
                        Text(text = exercise.exercise.name, fontWeight = FontWeight.Bold)
                        exercise.sets.forEach { set ->
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