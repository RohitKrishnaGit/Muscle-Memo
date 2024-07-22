package com.cs346.musclememo.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
@Composable
fun ExerciseTitle(
    exerciseRef: ExerciseRef,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = exerciseRef.name,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        IconButton(onClick = onClick) {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Delete Exercise",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun ExerciseSets(
    exerciseRef: ExerciseRef,
    sets: MutableList<ExerciseSet>,
    deleteSet: (Int) -> Unit,
    addSet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        data class FieldAttributes(val spacing: Float, val text: String)

        val dividedSpacing = 8f / (1 + (if (exerciseRef.weight) 1 else 0) + (if (exerciseRef.distance) 1 else 0))

        val setFields = mutableMapOf<String, FieldAttributes>().apply {
            put("set", FieldAttributes(1f, "Set"))

            if (exerciseRef.weight)
                put("weight", FieldAttributes(dividedSpacing, "Weight (kg)"))

            if (exerciseRef.distance)
                put("distance", FieldAttributes(dividedSpacing, "Distance (m)"))

            if (exerciseRef.durationVSReps)
                put("duration", FieldAttributes(dividedSpacing, "Duration (s)"))
            else
                put("reps", FieldAttributes(dividedSpacing, "Reps"))

            put("button", FieldAttributes(1f, ""))
        }

        if (sets.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                for (entry in setFields) {
                    Box(
                        modifier = Modifier.weight(entry.value.spacing),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry.value.text,
                            color = MaterialTheme.colorScheme.onSurface,
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
                        .weight(setFields["set"]?.spacing ?: 1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${setIndex + 1}",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                if (exerciseRef.weight) {
                    OutlinedTextField(
                        value = set.weight?.toString() ?: "",
                        onValueChange = { newValue ->
                            set.weight = newValue.toIntOrNull()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier
                            .weight(setFields["weight"]?.spacing ?: 1f)
                            .padding(start = 8.dp),
                    )
                }

                if (exerciseRef.distance) {
                    OutlinedTextField(
                        value = set.distance?.toString() ?: "",
                        onValueChange = { newValue ->
                            set.distance = newValue.toIntOrNull()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier
                            .weight(setFields["distance"]?.spacing ?: 1f)
                            .padding(start = 8.dp),
                    )
                }

                if (exerciseRef.durationVSReps) {
                    OutlinedTextField(
                        value = set.duration?.toString() ?: "",
                        onValueChange = { newValue ->
                            set.duration = newValue.toIntOrNull()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        modifier = Modifier
                            .weight(setFields["duration"]?.spacing ?: 1f)
                            .padding(start = 8.dp),
                    )
                } else {
                    OutlinedTextField(
                        value = set.reps?.toString() ?: "",
                        onValueChange = { newValue ->
                            set.reps = newValue.toIntOrNull()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .weight(setFields["reps"]?.spacing ?: 1f)
                            .padding(start = 8.dp),
                    )
                }

                IconButton(
                    onClick = { deleteSet(setIndex) },
                    modifier = Modifier.weight(setFields["button"]?.spacing ?: 1f)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Delete Set")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        MMButton(
            onClick = addSet,
            text = "Add Set",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}