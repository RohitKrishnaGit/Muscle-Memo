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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.screens.viewmodels.WorkoutScreenViewModel

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
    sets: MutableList<ExerciseSet>,
    deleteSet: (Int) -> Unit,
    addSet: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
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
                    value = set.weight?.toString() ?: "",
                    onValueChange = { newValue ->
                        set.weight = newValue.toIntOrNull()
                        println(newValue)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .weight(setFields[1].second)
                        .padding(start = 8.dp),
                )

                OutlinedTextField(
                    value = set.reps?.toString() ?: "",
                    onValueChange = { newValue ->
                        set.reps = newValue.toIntOrNull()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    modifier = Modifier
                        .weight(setFields[2].second)
                        .padding(start = 8.dp, end = 8.dp),
                    keyboardActions = KeyboardActions(onDone = {
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