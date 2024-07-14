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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
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
            .padding(start = 16.dp, end = 8.dp)
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
    sets: MutableList<ExerciseSet>,
    deleteSet: (Int) -> Unit,
    addSet: () -> Unit,
    editSet: (Int?, Int?, Int) -> Unit
) {
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
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        sets.forEachIndexed { setIndex, set ->
            var weight by remember { mutableStateOf(set.weight?.toString() ?: "") }
            var reps by remember { mutableStateOf(set.reps?.toString() ?: "") }
            val focusManager = LocalFocusManager.current

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
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                OutlinedTextField(
                    value = weight,
                    onValueChange = { newValue ->
                        weight = newValue
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .weight(setFields[1].second)
                        .padding(start = 8.dp),
                )

                OutlinedTextField(
                    value = reps,
                    onValueChange = { newValue ->
                        reps = newValue
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    modifier = Modifier
                        .weight(setFields[2].second)
                        .padding(start = 8.dp, end = 8.dp),
                    keyboardActions = KeyboardActions(onDone = {
                        editSet(weight.toIntOrNull(), reps.toIntOrNull(), setIndex)
                        focusManager.clearFocus()
                    }
                    )
                )

                IconButton(
                    onClick = { deleteSet(setIndex) },
                    modifier = Modifier.weight(setFields[3].second)
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

@Composable
fun DisplayExercises(
    exerciseRefs : List<ExerciseRef>
){
    val listState = rememberLazyListState()
    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = exerciseRefs){
            Row{
                Text(text = it.id.toString() + " ")
                Text(text = it.name)
            }
        }
    }
}