package com.cs346.musclememo.classes

import androidx.compose.runtime.mutableStateListOf

data class Exercise(
    val exerciseSet: MutableList<ExerciseSet> = mutableStateListOf<ExerciseSet>(ExerciseSet()),
    val workoutId: Int? = null,
    val templateId: Int? = null,
    val exerciseRefId: Int? = null,
    val customExerciseRefId: Int? = null
)
