package com.cs346.musclememo.classes

import androidx.compose.runtime.MutableState

data class Exercise(
    var weight: MutableState<Int?>,
    var reps: MutableState<Int?>
)