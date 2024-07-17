package com.cs346.musclememo.classes

data class User (
    val id: Int,
    val username : String,
    val email : String,
    val workouts: MutableList<Workout>?,
    val profilePicture: Any?,
    val gender: String,
    val experience: String
)