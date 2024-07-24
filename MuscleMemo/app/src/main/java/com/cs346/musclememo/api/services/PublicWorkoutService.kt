package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.screens.viewmodels.Message
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

data class CreatePublicWorkout(
    val name: String,
    val date: Int = 6,
    val experience: String,
    val latitude: String = "10",
    val longitude: String = "10",
    val description: String,
)

public interface  PublicWorkoutService {
    @POST("/publicWorkouts/me")
    fun createPublicWorkout(@Body body: CreatePublicWorkout): Call<ApiResponse<Int>>
}
