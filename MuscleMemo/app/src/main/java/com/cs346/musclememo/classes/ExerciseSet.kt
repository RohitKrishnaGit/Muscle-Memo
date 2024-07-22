package com.cs346.musclememo.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class ExerciseSet {
    var reps by  mutableStateOf<Int?>(null)
    var duration by  mutableStateOf<Int?>(null)
    var weight by mutableStateOf<Int?>(null)
    var distance by  mutableStateOf<Int?>(null)
}