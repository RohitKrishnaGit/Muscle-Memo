package com.cs346.musclememo.classes


data class ExerciseRef(
    val name: String = "Unknown Exercise",
    val id : Int = -1,
    val durationVSReps: Boolean = false,
    val weight: Boolean = false,
    val distance: Boolean = false
)