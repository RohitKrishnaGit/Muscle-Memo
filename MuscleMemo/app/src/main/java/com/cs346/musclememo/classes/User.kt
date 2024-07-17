package com.cs346.musclememo.classes

data class User (
    val id: Int,
    val username : String,
    val fullName : String,
    val email : String,
    val gender: String,
    val experience: String,
    val workouts: MutableList<Workout>
)