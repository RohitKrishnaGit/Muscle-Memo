package com.cs346.musclememo.classes

import androidx.compose.runtime.mutableStateListOf

data class ExerciseIteration (
    val exercise : Exercise,
    var sets : MutableList<ExerciseSet> = mutableStateListOf<ExerciseSet>(ExerciseSet())
)