package com.cs346.musclememo.classes

import android.net.Uri

data class User (
    val id: Int,
    val username : String,
    val email : String,
    val workouts: MutableList<Workout>?,
    val profilePicture: Uri?,
    val gender: String,
    val experience: String
)