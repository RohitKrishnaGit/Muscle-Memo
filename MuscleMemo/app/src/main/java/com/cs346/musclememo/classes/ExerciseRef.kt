package com.cs346.musclememo.classes


data class ExerciseRef(
    val name: String = "Unknown Exercise",
    val id : Int = -1,
    val durationVSReps: Int = 0,
    val weight: Int = 0,
    val distance: Int = 0
)