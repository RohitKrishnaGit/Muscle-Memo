package com.cs346.musclememo.classes

import androidx.compose.runtime.mutableStateListOf

data class ExerciseIteration (
    val exerciseRef : ExerciseRef,
    val customExerciseRef: CustomExerciseRef? = null, //TODO need to think about this
    val templateId: Int? = null, //TODO need to think about this
    var exerciseSet : MutableList<ExerciseSet> = mutableStateListOf<ExerciseSet>(ExerciseSet())
)