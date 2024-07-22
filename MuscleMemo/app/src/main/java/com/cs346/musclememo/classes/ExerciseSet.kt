package com.cs346.musclememo.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class ExerciseSet(
    var initialReps: Int? = null,
    var initialDuration: Int? = null,
    var initialWeight: Int? = null,
    var initialDistance: Int? = null
) {
    var reps by  mutableStateOf<Int?>(initialReps)
    var duration by  mutableStateOf<Int?>(initialDuration)
    var weight by mutableStateOf<Int?>(initialWeight)
    var distance by  mutableStateOf<Int?>(initialDistance)
}