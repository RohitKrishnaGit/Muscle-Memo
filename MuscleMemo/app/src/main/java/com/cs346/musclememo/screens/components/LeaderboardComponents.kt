package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.screens.viewmodels.LeaderboardScreenViewModel

@Composable
fun LeaderboardHeading() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Rank",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "Name",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "Score",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChooseLeaderboardExercise(
    viewModel: LeaderboardScreenViewModel
) {
    val localFocusManager = LocalFocusManager.current
    AnimatedVisibility(
        visible = viewModel.chooseExerciseVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                ) {
                    IconButton(onClick = { viewModel.updateChooseExerciseVisible(false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                    OutlinedTextField(
                        value = viewModel.exerciseSearchText,
                        onValueChange = viewModel::updateExerciseSearchText,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    IconButton(onClick = { viewModel.toggleSort() }) {
                        if (viewModel.isSortedAlphabetically) {
                            Icon(
                                Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort Ascending",
                            )
                        } else {
                            Icon(
                                Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort Descending",
                                modifier = Modifier.rotate(180f),
                            )
                        }
                    }
                }
                if (viewModel.exerciseSearchText == ""){
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Your Completed Exercises")
                    }
                }
                LazyColumn {
                    val groupedRefs = if (viewModel.exerciseSearchText != ""){
                        viewModel.userCompletedExerciseRefs
                    } else {
                        viewModel.sortedExerciseRefs
                    }.groupBy { it.name.first().uppercase() }

                    groupedRefs.forEach{ (initial, refs) ->
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
                        items(items = refs){ref ->
                            Row (
                                modifier = Modifier.fillMaxWidth().clickable {
                                    localFocusManager.clearFocus()
                                    viewModel.updateCurrentExerciseRef(ref)
                                    viewModel.updateChooseExerciseVisible(false)
                                    viewModel.updateExerciseSearchText("")
                                }
                            ) {
                                Text(text = ref.name)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayLeaderboardExercise(
    viewModel: LeaderboardScreenViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null, onClick = {
                viewModel.updateChooseExerciseVisible(true)
            })
    ) {
        OutlinedTextField(
            value = viewModel.currentExerciseRef.name,
            onValueChange = {},
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ChooseLeaderboardType(
    viewModel: LeaderboardScreenViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        ChooseLeaderboardTypeButton(
            text = "Global",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.leaderboardType == "Global",
            onClick = {
                if (viewModel.leaderboardType != "Global")
                    viewModel.updateLeaderboardType("Global")
            }
        )
        ChooseLeaderboardTypeButton(
            text = "Friends",
            modifier = Modifier.weight(1f),
            highlighted = viewModel.leaderboardType == "Friends",
            onClick = {
                if (viewModel.leaderboardType != "Friends")
                    viewModel.updateLeaderboardType("Friends")
            }
        )
    }
}

@Composable
fun ChooseLeaderboardTypeButton(
    text: String,
    highlighted: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (highlighted) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text)
        }
    }
}