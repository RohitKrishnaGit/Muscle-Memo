package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.time.Duration

data class WorkoutRequest (val name: String, val date: Long, val duration: Int, val userId: String = "me")
data class WorkoutResponse (val workoutId: Int)

interface  WorkoutService {
    @POST("/workouts")
    fun createWorkout(@Body workoutRequest: WorkoutRequest): Call<ApiResponse<WorkoutResponse>>

    @GET("/workouts/me")
    fun getWorkoutByUserId (): Call<ApiResponse<List<Workout>>>
}